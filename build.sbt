/**/
import sbt._
import Dependencies._

val _name = "Education System Management"
val _version = "0.0.1"

lazy val baseSettings = Seq(
  name := _name,
  version := _version,
  organization := "com.ems",
  scalaVersion := "2.13.1"
)

lazy val companyMgt = project.in(file(companyMgtMod))
lazy val user = project.in(file(userMod))

val rootDependencies =
  akkaDep ++
  sprayJsonDep ++
  pureConfigDep ++
  testingDep ++
  loggingDep ++
  doobieDep

lazy val root = (project in file("."))
  .settings(name := _name)
  .aggregate(companyMgt, user)
  .dependsOn(companyMgt, user)
  .settings(libraryDependencies ++= rootDependencies)
