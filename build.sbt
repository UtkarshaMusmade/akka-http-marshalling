name := "akka-http-marshalling"

version := "0.1"

scalaVersion := "2.12.6"


libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.5.23"
libraryDependencies +="com.typesafe.akka" %% "akka-stream" % "2.5.23"
libraryDependencies += "com.typesafe.akka" %% "akka-http" % "10.1.9"

libraryDependencies += "com.typesafe.play" %% "play-json" % "2.6.7"

libraryDependencies +=   "de.heikoseeberger" %% "akka-http-play-json" % "1.20.0"

libraryDependencies += "com.typesafe.akka" %% "akka-http-spray-json" % "10.1.9"

libraryDependencies += "org.json4s" %% "json4s-native" % "3.2.11"

libraryDependencies += "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.9.9"


libraryDependencies += "com.fasterxml.jackson.core" % "jackson-databind" % "2.2.2"