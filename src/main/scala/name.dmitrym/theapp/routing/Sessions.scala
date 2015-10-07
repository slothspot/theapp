package name.dmitrym.theapp.routing

import akka.http.scaladsl.server.Directives._

object Sessions extends Router{
  val login = put {
    complete(Responses.Stub)
  }

  val logout = post {
    complete(Responses.Stub)
  }

  def route = path("sessions") {
    login ~ logout
  }
}
