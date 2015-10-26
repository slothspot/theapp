package name.dmitrym.theapp.routing

import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import com.typesafe.scalalogging.LazyLogging

class Users(implicit mat: ActorMaterializer) extends Router with LazyLogging {
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

object Users {
  def apply()(implicit materializer: ActorMaterializer) = new Users
}