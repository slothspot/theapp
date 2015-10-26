package name.dmitrym.theapp.routing

import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import com.typesafe.scalalogging.LazyLogging

class Calendar(implicit mat: ActorMaterializer) extends Router with LazyLogging {
  private[this] val addEventTimer = metrics.timer("addEvent")
  val addEvent = addEventTimer.time {put {
    complete(Responses.Stub)
  }}

  private[this] val updateEventTimer = metrics.timer("updateEvent")
  val updateEvent = updateEventTimer.time { post {
    complete(Responses.Stub)
  }}

  private[this] val getEventsTimer = metrics.timer("getEvents")
  val getEvents = getEventsTimer.time{ get {
    complete(Responses.Stub)
  }}

  def route = path("calendar") {
    addEvent ~ updateEvent ~ getEvents
  }
}

object Calendar {
  def apply()(implicit materializer: ActorMaterializer) = new Calendar
}