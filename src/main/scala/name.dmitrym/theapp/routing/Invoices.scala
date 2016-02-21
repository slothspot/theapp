package name.dmitrym.theapp.routing

import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import com.mongodb.casbah.commons.MongoDBObject
import com.mongodb.util.JSON
import com.typesafe.scalalogging.LazyLogging
import name.dmitrym.theapp.storage.{InvoiceStatus, Storage}
import com.softwaremill.session.SessionDirectives._
import com.softwaremill.session.SessionOptions._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import name.dmitrym.theapp.utils.Marshallers._
import org.bson.types.ObjectId
import org.joda.time.DateTime
import com.mongodb.casbah.Imports._

class Invoices(implicit mat: ActorMaterializer) extends Router with LazyLogging {
  import Sessions.sessionManager
  private[this] val storage = Storage()

  private[this] def createInvoiceFromPayload(inv: InvoiceCreatePayload) =
    MongoDBObject(
      "title" -> inv.title,
      "reqType" -> inv.reqType,
      "reqNeed" -> inv.reqNeed,
      "reqDescription" -> inv.reqDescription,
      "priority" -> inv.priority,
      "status" -> InvoiceStatus.Created.id,
      "creationTime" -> new DateTime(),
      "quantity" -> inv.quantity,
      "assigneeId" -> inv.assigneeId,
      "creatorId" -> inv.creatorId,
      "companyId" -> inv.companyId
    )

  private[this] def updateInvoiceFromPayload(o: MongoDBObject, inv: InvoiceUpdatePayload) = {
    o += (
      "title" -> inv.title,
      "reqType" -> inv.reqType,
      "reqNeed" -> inv.reqNeed,
      "reqDescription" -> inv.reqDescription,
      "priority" -> Integer.valueOf(inv.priority),
      "quantity" -> Integer.valueOf(inv.quantity),
      "assigneeId" -> inv.assigneeId,
      "creatorId" -> inv.creatorId,
      "companyId" -> inv.companyId,
      "creationTime" -> new DateTime(inv.creationTime),
      "status" -> Integer.valueOf(InvoiceStatus.Updated.id)
    )
    o
  }

  private[this] val createInvoiceTimer = metrics.timer("createInvoice")
  val createInvoice = createInvoiceTimer.time { put { requiredSession(oneOff, usingCookies) { session =>
    (requestEntityPresent & entity(as[InvoiceCreatePayload])) { (inv) =>
      storage.sessions.findOne(MongoDBObject("sessionId" -> session)) match {
        case Some(s) =>
          val obj = createInvoiceFromPayload(inv)
          storage.invoices.insert(obj)
          complete(Responses.InvoiceCreated(obj.getAs[ObjectId]("_id").get.toString))
        case None => complete(Responses.NotAuthorized)
      }
    }
  }}}

  private[this] val updateInvoiceTimer = metrics.timer("updateInvoice")
  val updateInvoice = updateInvoiceTimer.time { post { requiredSession(oneOff, usingCookies) { session =>
    (requestEntityPresent & entity(as[InvoiceUpdatePayload])) { (inv) =>
      if(inv.id.isEmpty){
        complete(Responses.Fail("Missing Id"))
      } else {
        storage.invoices.findOneByID(new ObjectId(inv.id)) match {
          case Some(e) =>
            val upd = updateInvoiceFromPayload(e, inv)
            storage.invoices.update(e, upd)
            complete(Responses.InvoiceUpdated(upd.getAs[ObjectId]("_id").get.toString))
          case None => complete(Responses.Fail("Invoice with given Id doesn't exist"))
        }
      }
    }
  }}}

  private[this] val invoiceStatusTimer = metrics.timer("invoiceStatus")
  val invoiceStatus = invoiceStatusTimer.time { get { requiredSession(oneOff, usingCookies) { session =>
    storage.sessions.findOne(MongoDBObject("sessionId" -> session)) match {
      case Some(s) =>
        complete {
          s.get("role").asInstanceOf[Int] match {
            case 0 =>
              storage.invoices.find().map { i =>
                val assigneeId = i.getAs[String]("assigneeId").get
                val creatorId = i.getAs[String]("creatorId").get
                val assignee = storage.users.findOne(MongoDBObject("_id" -> new ObjectId(assigneeId))).get.getAs[String]("name").get
                val creator = storage.users.findOne(MongoDBObject("_id" -> new ObjectId(creatorId))).get.getAs[String]("name").get
                JSON.serialize(i ++ ("assignee" -> assignee, "creator" -> creator))
              }.toArray.mkString("[", ",", "]")
            case _ =>
              storage.invoices.find(MongoDBObject("companyId" -> s.getAs[String]("company").get)).map { i =>
                val assigneeId = i.getAs[String]("assigneeId").get
                val creatorId = i.getAs[String]("creatorId").get
                val assignee = storage.users.findOne(MongoDBObject("_id" -> new ObjectId(assigneeId))).get.getAs[String]("name").get
                val creator = storage.users.findOne(MongoDBObject("_id" -> new ObjectId(creatorId))).get.getAs[String]("name").get
                JSON.serialize(i ++ ("assignee" -> assignee, "creator" -> creator))
              }.toArray.mkString("[", ",", "]")
          }
        }
      case None =>
        complete(Responses.NotAuthorized)
    }
  }}}

  private[this] val getInvoiceInfoTimer = metrics.timer("getInvoiceInfo")
  val getInvoiceInfo = getInvoiceInfoTimer.time {
    get { requiredSession(oneOff, usingCookies) { session =>
      extractUnmatchedPath { invoiceId =>
        storage.sessions.findOne(MongoDBObject("sessionId" -> session)) match {
          case Some(s) =>
            storage.invoices.findOne(MongoDBObject("_id" -> new ObjectId(invoiceId.tail.toString))) match {
              case Some(i) =>
                val inv = JSON.serialize(i ++ ("id" -> i.getAs[ObjectId]("_id").get.toHexString))
                complete(inv)
              case None => complete(Responses.DoesntExist)
            }
          case None => complete(Responses.NotAuthorized)
        }
      }
    }}
  }

  def route = path("invoices") {
    createInvoice ~ updateInvoice ~ invoiceStatus
  } ~ pathPrefix("invoice") {
    getInvoiceInfo
  }
}

object Invoices {
  def apply()(implicit materializer: ActorMaterializer) = new Invoices
}
