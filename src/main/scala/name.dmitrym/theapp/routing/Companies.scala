package name.dmitrym.theapp.routing

import akka.http.scaladsl.server.Directives._

object Companies extends Router {
  val create = put {
    complete(Responses.Stub)
  }

  val update = post {
    complete(Responses.Stub)
  }

  val stats = get {
    complete(Responses.Stub)
  }

  def route = path("companies") {
    create ~ update ~ stats
  }
}
