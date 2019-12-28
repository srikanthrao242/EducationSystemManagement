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
lazy val employee = project.in(file(employeeMod))
lazy val timeMgt = project.in(file(timeManagementMod))

val rootDependencies =
  akkaDep ++
  sprayJsonDep ++
  pureConfigDep ++
  testingDep ++
  loggingDep ++
  doobieDep

lazy val service = (project in file(serviceMod))
  .settings(name := _name)
  .aggregate(companyMgt, user, employee, timeMgt)
  .dependsOn(companyMgt, user, employee, timeMgt)
  .settings(libraryDependencies ++= rootDependencies)
