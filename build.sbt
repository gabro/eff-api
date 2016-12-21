name := "error"

scalaVersion := "2.12.1"

libraryDependencies ++= Seq(
  "org.atnos" %% "eff" % "2.2.0",
  "com.typesafe.akka" %% "akka-http" % "10.0.0",
  "de.heikoseeberger" %% "akka-http-circe" % "1.11.0",
  "io.circe" %% "circe-core" % "0.6.1",
  "io.circe" %% "circe-generic" % "0.6.1",
  "io.circe" %% "circe-parser" % "0.6.1",
  "com.typesafe.slick" %% "slick" % "3.2.0-M2",
  "com.h2database" % "h2" % "1.4.187",
  "org.slf4j" % "slf4j-nop" % "1.6.4",
  "org.backuity" %% "ansi-interpolator" % "1.1.0"
)

addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.9.3")

scalacOptions += "-Ypartial-unification"
