import Dependencies._

val _name = serviceMod
val _version = "0.0.1"

lazy val baseSettings = Seq(
  name := _name,
  version := _version,
  organization := "com.ems",
  scalaVersion := "2.13.1"
)

lazy val service =
  project
    .in(file("."))
    .settings(name := _name)
    .settings(
      libraryDependencies ++= akkaDep
    )
