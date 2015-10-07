package name.dmitrym.theapp.routing

import akka.http.scaladsl.server.Directives._

object Users extends Router {
  val create = put {
    complete(Responses.Stub)
  }

  val update = post {
    complete(Responses.Stub)
  }

  val stats = get {
    complete(Responses.Stub)
  }

  def route = path("users") {
    create ~ update ~ stats
  }
}
