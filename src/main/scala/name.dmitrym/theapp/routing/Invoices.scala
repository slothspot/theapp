package name.dmitrym.theapp.routing

import akka.http.scaladsl.server.Directives._

object Invoices extends Router {
  val create = put {
    complete(Responses.Stub)
  }

  val update = post {
    complete(Responses.Stub)
  }

  val status = get {
    complete(Responses.Stub)
  }

  def route = path("invoices") {
    create ~ update ~ status
  }
}
