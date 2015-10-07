package name.dmitrym.theapp.routing

import akka.http.scaladsl.server.Route
import name.dmitrym.theapp.utils.Instrumented

trait Router extends Instrumented {
  def route:Route
}
