package name.dmitrym.theapp

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer

import scala.io.StdIn

object TheApp extends App {
  implicit val system = ActorSystem("theApp")
  implicit val materializer = ActorMaterializer()

  val route = (path("hello") & get) {
    complete("ok")
  }

  val bindingFuture = Http().bindAndHandle(route, "localhost", 8080)

  StdIn.readLine()

  import system.dispatcher
  bindingFuture.flatMap(_.unbind()).onComplete( _ =>system.shutdown() )
}
