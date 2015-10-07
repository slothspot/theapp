package name.dmitrym.theapp.routing

import akka.http.scaladsl.server.Directives._

object CompanyEvents extends Router {
  val createEvent = put {
    complete(Responses.Stub)
  }

  val updateEvent = post {
    complete(Responses.Stub)
  }

  val getEvent = get {
    complete(Responses.Stub)
  }

  def route = path("cmpEvents") {
    createEvent ~ updateEvent ~ getEvent
  }
}
