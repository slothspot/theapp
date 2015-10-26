package name.dmitrym.theapp.routing

import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import com.typesafe.scalalogging.LazyLogging

class CompanyEvents(implicit mat: ActorMaterializer) extends Router with LazyLogging {
  private[this] val createCompanyEventTimer = metrics.timer("createCompanyEvent")
  val createCompanyEvent = createCompanyEventTimer.time { put {
    complete(Responses.Stub)
  }}

  private[this] val updateCompanyEventTimer = metrics.timer("updateCompanyEvent")
  val updateCompanyEvent = updateCompanyEventTimer.time { post {
    complete(Responses.Stub)
  }}

  private[this] val getCompanyEventsTimer = metrics.timer("getCompanyEvents")
  val getCompanyEvents = getCompanyEventsTimer.time { get {
    complete(Responses.Stub)
  }}

  def route = path("cmpEvents") {
    createCompanyEvent ~ updateCompanyEvent ~ getCompanyEvents
  }
}

object CompanyEvents {
  def apply()(implicit materializer: ActorMaterializer) = new CompanyEvents
}