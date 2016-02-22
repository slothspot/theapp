package name.dmitrym.theapp.routing

import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.StreamConverters
import com.mongodb.casbah.Imports._
import com.mongodb.casbah.gridfs.Imports._
import com.typesafe.scalalogging.LazyLogging
import com.softwaremill.session.SessionDirectives._
import com.softwaremill.session.SessionOptions._
import name.dmitrym.theapp.storage.Storage

class Upload(implicit mat: ActorMaterializer) extends Router with LazyLogging {
  import Sessions.sessionManager
  private[this] val storage = Storage()
  private[this] val handleFileUploadTimer = metrics.timer("handleFileUpload")
  val handleFileUpload = handleFileUploadTimer.time {
    put {
      requiredSession(oneOff, usingCookies) { session =>
        formField('invoiceId) { invoiceId =>
          storage.invoices.findOne(MongoDBObject("_id" -> new ObjectId(invoiceId))) match {
            case Some(i) =>
              uploadedFile("file") { case (metadata, file) =>
                logger.debug(s"Received file upload for invoice: $invoiceId => ${metadata.fileName}")
                complete {
                  val fid = storage.attachments(file){ f =>
                    f.contentType = metadata.contentType.mediaType.toString()
                    f.filename = invoiceId + "_" + metadata.fileName
                  }
                  val attachmentId = fid.get.asInstanceOf[ObjectId].toHexString
                  val attachments = i.getAsOrElse("attachments", Array[String]()) :+ attachmentId
                  storage.invoices.update(i, i ++ ("attachments" -> attachments))
                  Responses.UploadCompleted(attachmentId)
                }
              }
            case None => complete(Responses.DoesntExist)
          }
        }
      }
    }
  }
  private[this] val getAttachmentTimer = metrics.timer("getAttachment")
  val getAttachment = getAttachmentTimer.time {
    get { requiredSession(oneOff, usingCookies) { session =>
      storage.sessions.findOne(MongoDBObject("sessionId" -> session)) match {
        case Some(s) =>
          extractUnmatchedPath { attachmentId =>
            storage.attachments.findOne(new ObjectId(attachmentId.tail.toString)) match {
              case Some(f) =>
                ContentType.parse(f.contentType.get) match {
                  case Right(ct) =>
                    complete(HttpResponse(entity = HttpEntity(
                      ct, f.length, StreamConverters.fromInputStream(() => f.inputStream)
                    )))
                  case _ => complete(Responses.NotAllowed)
                }
              case None => complete(Responses.DoesntExist)
            }
          }
        case None =>
          complete(Responses.NotAuthorized)
      }
    }}
  }

  def route = path("upload") {
    handleFileUpload
  } ~ pathPrefix("attachment") {
    getAttachment
  }
}

object Upload {
  def apply()(implicit materializer: ActorMaterializer) = new Upload
}