name := """learn-scala"""
organization := "com.example"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.11"

libraryDependencies += guice
libraryDependencies += {
  "org.scalatestplus.play" %% "scalatestplus-play" % "5.1.0" % Test
  "org.mongodb.scala" %% "mongo-scala-driver" % "4.10.2"
}

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.example.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.example.binders._"
