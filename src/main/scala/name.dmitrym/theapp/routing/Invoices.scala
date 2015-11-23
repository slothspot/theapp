package name.dmitrym.theapp.routing

import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import com.mongodb.casbah.commons.MongoDBObject
import com.typesafe.scalalogging.LazyLogging
import name.dmitrym.theapp.storage.{InvoiceStatus, Storage}
import com.softwaremill.session.SessionDirectives._
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
      "companyId" -> inv.companyId,
      "userId" -> inv.userId,
      "title" -> inv.title,
      "reqType" -> inv.reqType,
      "reqNeed" -> inv.reqNeed,
      "reqDescription" -> inv.reqDescription,
      "reqImg" -> inv.reqImg,
      "priority" -> inv.priority,
      "status" -> InvoiceStatus.Created.id,
      "creationTime" -> new DateTime()
    )

  private[this] def updateInvoiceFromPayload(o: MongoDBObject, inv: InvoiceUpdatePayload) = {
    o += (
      "title" -> inv.title,
      "reqType" -> inv.reqType,
      "reqNeed" -> inv.reqNeed,
      "reqDescription" -> inv.reqDescription,
      "reqImg" -> inv.reqImg,
      "startTime" -> new DateTime(inv.startTime),
      "finishTime" -> new DateTime(inv.finishTime),
      "status" -> Integer.valueOf(InvoiceStatus.Updated.id),
      "responsible" -> inv.responsible,
      "result" -> inv.result
    )
    o
  }

  private[this] val createInvoiceTimer = metrics.timer("createInvoice")
  val createInvoice = createInvoiceTimer.time { put { requiredSession() { session =>
    (requestEntityPresent & entity(as[InvoiceCreatePayload])) { (inv) =>
      val obj = createInvoiceFromPayload(inv)
      storage.invoices.insert(obj)
      complete(Responses.InvoiceCreated(obj.getAs[ObjectId]("_id").get.toString))
    }
  }}}

  private[this] val updateInvoiceTimer = metrics.timer("updateInvoice")
  val updateInvoice = updateInvoiceTimer.time { post { requiredSession() { session =>
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
  val invoiceStatus = invoiceStatusTimer.time { get { requiredSession() { session =>
    complete(Responses.Stub)
  }}}

  def route = path("invoices") {
    createInvoice ~ updateInvoice ~ invoiceStatus
  }
}

object Invoices {
  def apply()(implicit materializer: ActorMaterializer) = new Invoices
}