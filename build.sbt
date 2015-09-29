name := "theapp"

organization := "name.dmitrym"

version := "0.1-SNAPSHOT"

libraryDependencies ++= Seq(
  "com.typesafe.scala-logging" %% "scala-logging" % "3.1.0",
  "ch.qos.logback" % "logback-classic" % "1.1.3",
  "com.typesafe" % "config" % "1.3.0",
  "com.typesafe.akka" %% "akka-http-experimental" % "1.0",
  "org.mongodb.scala" %% "mongo-scala-driver" % "1.0.0-rc0",
  "org.specs2" %% "specs2-core" % "3.6.4" % "test"
  )

scalaVersion := "2.11.7"

scalacOptions ++= Seq(
  "-deprecation"
  )

javacOptions ++= Seq(
  )

incOptions := incOptions.value.withNameHashing(true)
