package name.dmitrym.theapp.routing

import akka.http.scaladsl.server.Directives._

object Users extends Router {
  private[this] val createUserTimer = metrics.timer("createUser")
  val createUser = createUserTimer.time { put {
    complete(Responses.Stub)
  }}

  private[this] val updateUserTimer = metrics.timer("updateUser")
  val updateUser = updateUserTimer.time { post {
    complete(Responses.Stub)
  }}

  private[this] val userStatsTimer = metrics.timer("userStats")
  val userStats = userStatsTimer.time { get {
    complete(Responses.Stub)
  }}

  def route = path("users") {
    createUser ~ updateUser ~ userStats
  }
}
