/**/
import Dependencies._

val _name = userMod
val _version = "0.0.1"

lazy val baseSettings = Seq(
  name := _name,
  version := _version,
  organization := "com.ems",
  scalaVersion := "2.13.1"
)
resolvers += Resolver.url(
  "bintray-com.ems-ems_utilities",
  url("https://dl.bintray.com/srikanthrao242/ems_utilities"))(
  Resolver.ivyStylePatterns)

lazy val user =
  project
    .in(file("."))
    .settings(name := _name)
    .settings(
      libraryDependencies ++= (akkaDep ++ loggingDep ++ pureConfigDep ++ doobieDep ++ emsUtils)
    )
