package name.dmitrym.theapp.routing

import akka.http.scaladsl.server.Directives._

object Invoices extends Router {
  private[this] val createInvoiceTimer = metrics.timer("createInvoice")
  val createInvoice = createInvoiceTimer.time { put {
    complete(Responses.Stub)
  }}

  private[this] val updateInvoiceTimer = metrics.timer("updateInvoice")
  val updateInvoice = updateInvoiceTimer.time { post {
    complete(Responses.Stub)
  }}

  private[this] val invoiceStatusTimer = metrics.timer("invoiceStatus")
  val invoiceStatus = invoiceStatusTimer.time { get {
    complete(Responses.Stub)
  }}

  def route = path("invoices") {
    createInvoice ~ updateInvoice ~ invoiceStatus
  }
}
