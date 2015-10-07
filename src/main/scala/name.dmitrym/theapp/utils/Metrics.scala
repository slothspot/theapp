package name.dmitrym.theapp.utils

import name.dmitrym.theapp.TheApp

trait Instrumented extends nl.grons.metrics.scala.InstrumentedBuilder {
  val metricRegistry = TheApp.metricRegistry
}