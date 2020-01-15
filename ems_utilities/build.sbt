val _name = "ems_utilities"
val _version = "0.0.2"

val spray_json = "1.3.5"
val pure_config = "0.12.1"
val doobie_ver = "0.8.6"
val akka = "2.6.1"
val akka_http = "10.1.11"

lazy val baseSettings = Seq(
  name := _name,
  version := _version,
  organization := "com.ems",
  scalaVersion := "2.13.1"
)

val sprayJsonDep = Seq(
  "io.spray" %% "spray-json" % spray_json
)
val pureConfigDep = Seq(
  "com.github.pureconfig" %% "pureconfig" % pure_config
)

val doobieDep = Seq(
  "org.tpolecat" %% "doobie-core"         % doobie_ver
)

val akkaDep = Seq(
  "com.typesafe.akka" %% "akka-actor"           % akka,
  "com.typesafe.akka" %% "akka-stream"          % akka,
  "com.typesafe.akka" %% "akka-http"            % akka_http,
  "com.typesafe.akka" %% "akka-http-spray-json" % akka_http
)
val rootDependencies = sprayJsonDep ++ pureConfigDep ++ doobieDep ++ akkaDep

ThisBuild / version := _version
ThisBuild / organization := "com.ems"
ThisBuild / description := "utilities for ems"

// This is an example.  sbt-bintray requires licenses to be specified
// (using a canonical name).

lazy val service = (project in file("."))
  .settings(name := _name,
    publishMavenStyle := false,
    bintrayRepository := "ems_utilities",
    bintrayOmitLicense := true,
    bintrayOrganization in bintray := None)
  .settings(libraryDependencies ++= rootDependencies)