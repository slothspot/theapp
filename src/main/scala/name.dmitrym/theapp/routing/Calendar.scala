package name.dmitrym.theapp.routing

import akka.http.scaladsl.server.Directives._

object Calendar extends Router {
  val addEvent = put {
    complete(Responses.Stub)
  }
  val updateEvent = post {
    complete(Responses.Stub)
  }
  val getEvents = get {
    complete(Responses.Stub)
  }

  def route = path("calendar") {
    addEvent ~ updateEvent ~ getEvents
  }
}
