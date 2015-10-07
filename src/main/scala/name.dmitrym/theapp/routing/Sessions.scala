package name.dmitrym.theapp.routing

import akka.http.scaladsl.server.Directives._

object Sessions extends Router{
  private[this] val loginTimer = metrics.timer("login")
  val login = loginTimer.time { put {
    complete(Responses.Stub)
  }}

  private[this] val logoutTimer = metrics.timer("logout")
  val logout = logoutTimer.time { post {
    complete(Responses.Stub)
  }}

  def route = path("sessions") {
    login ~ logout
  }
}
