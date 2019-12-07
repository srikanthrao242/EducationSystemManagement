
val _name = "Education System Management"
val _version = "0.0.1"

lazy val baseSettings = Seq(
  name := _name,
  version := _version,
  organization := "com.education.system",
  scalaVersion := "2.13.1"
)

lazy val service = project.in(file("service"))

lazy val companyMgt = project.in(file("company_management"))

lazy val root = (project in file(".")).settings(name := _name)
  .aggregate(service,companyMgt)


