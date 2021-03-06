name := "theapp"

organization := "name.dmitrym"

version := "0.1-SNAPSHOT"

libraryDependencies ++= Seq(
  "com.typesafe.scala-logging" %% "scala-logging" % "3.1.0",
  "ch.qos.logback" % "logback-classic" % "1.1.3",
  "com.typesafe" % "config" % "1.3.0",
  "com.typesafe.akka" %% "akka-http-experimental" % "2.4.2",
  "com.softwaremill.akka-http-session" %% "core" % "0.2.4",
  "com.typesafe.akka" %% "akka-http-spray-json-experimental" % "2.4.2",
  "org.mongodb" %% "casbah" % "3.1.0",
  "nl.grons" %% "metrics-scala" % "3.5.2_a2.3",
  "org.specs2" %% "specs2-core" % "3.6.5" % "test",
  "org.specs2" %% "specs2-scalacheck" % "3.6.5" % "test",
  //web jars
  "org.webjars" % "jquery" % "1.12.0",
  "org.webjars" % "jquery-knob" % "1.2.11",
  "org.webjars" % "bootstrap" % "3.3.6",
  "org.webjars" % "font-awesome" % "4.5.0",
  "org.webjars" % "angularjs" % "1.5.0",
  "org.webjars" % "angular-ui-router" % "0.2.17",
  "org.webjars.bower" % "md5" % "0.3.0",
  "org.webjars" % "datatables" % "1.10.10",
  "org.webjars" % "angular-datatables" % "0.5.2",
  "org.webjars" % "angular-material" % "1.0.5"
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

cancelable in Global := true

test in assembly := {}
