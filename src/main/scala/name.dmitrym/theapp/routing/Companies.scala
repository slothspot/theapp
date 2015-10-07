package name.dmitrym.theapp.routing

import akka.http.scaladsl.server.Directives._

object Companies extends Router {
  private[this] val createCompanyTimer = metrics.timer("createCompanyTimer")
  val createCompany = createCompanyTimer.time { put {
    complete(Responses.Stub)
  }}

  private[this] val updateCompanyTimer = metrics.timer("updateCompanyTimer")
  val updateCompany = updateCompanyTimer.time { post {
    complete(Responses.Stub)
  }}

  private[this] val companiesInfoTimer = metrics.timer("companiesInfoTimer")
  val companiesInfo = companiesInfoTimer.time { get {
    complete(Responses.Stub)
  }}

  def route = path("companies") {
    createCompany ~ updateCompany ~ companiesInfo
  }
}
