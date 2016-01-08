name := "theapp"

organization := "name.dmitrym"

version := "0.1-SNAPSHOT"

libraryDependencies ++= Seq(
  "com.typesafe.scala-logging" %% "scala-logging" % "3.1.0",
  "ch.qos.logback" % "logback-classic" % "1.1.3",
  "com.typesafe" % "config" % "1.3.0",
  "com.typesafe.akka" %% "akka-http-experimental" % "2.0.1",
  "com.softwaremill.akka-http-session" %% "core" % "0.2.3",
  "com.typesafe.akka" %% "akka-http-spray-json-experimental" % "2.0.1",
  "org.mongodb" %% "casbah" % "3.1.0",
  "nl.grons" %% "metrics-scala" % "3.5.2_a2.3",
  "org.specs2" %% "specs2-core" % "3.6.5" % "test",
  "org.specs2" %% "specs2-scalacheck" % "3.6.5" % "test"
  )

scalaVersion := "2.11.7"

scalacOptions ++= Seq(
  "-deprecation", "-feature", "-optimise", "-unchecked", "-Xlint:_"
  )

scalacOptions in Test ++= Seq(
  "-Yrangepos"
  )

javacOptions ++= Seq(
  "-deprecation", "-Xlint"
  )

incOptions := incOptions.value.withNameHashing(true)

parallelExecution in Test := false

coverageEnabled in Test := true

coverageHighlighting in Test := true
