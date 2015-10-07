package name.dmitrym.theapp

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.LazyLogging

import scala.io.StdIn

object TheApp extends App with LazyLogging {
  implicit val system = ActorSystem("theApp")
  implicit val materializer = ActorMaterializer()

  val route = path("public" / Rest) { r =>
    getFromResource("public/" + r)
  } ~ path("public") {
    redirect("public/index.html", StatusCodes.MovedPermanently)
  }

  val config = ConfigFactory.load("application.conf")
  assert(config.hasPath("service.interface"), "Config doesn't have service.interface specified")
  val listenInterface = config.getString("service.interface")
  assert(config.hasPath("service.port"), "Config doesn't have service.port specified")
  val listenPort = config.getInt("service.port")
  val bindingFuture = Http().bindAndHandle(route, listenInterface, listenPort)

  StdIn.readLine()

  import system.dispatcher
  bindingFuture.flatMap(_.unbind()).onComplete( _ => system.shutdown() )
}