package name.dmitrym.theapp.routing

import akka.http.scaladsl.server.Directives._

object CompanyEvents extends Router {
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
