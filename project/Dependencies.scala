/**/
import sbt._
object Dependencies {

  val serviceMod = "service"
  val companyMgtMod = "company_management"
  val userMod = "user_management"
  val employeeMod = "employee_management"
  val timeManagementMod = "time_management"
  val ems_entitiesMod = "ems_entities"

  val akka = "2.6.1"
  val akka_http = "10.1.11"
  val logback = "1.2.3"
  val spray_json = "1.3.5"
  val pure_config = "0.12.1"
  val scala_test = "3.0.8"
  val scala_mock = "4.4.0"
  val scalaz_ver = "7.2.29"
  val doobie_ver = "0.8.6"
  val mysql = "8.0.15"

  val akkaDep = Seq(
    "com.typesafe.akka" %% "akka-actor"           % akka,
    "com.typesafe.akka" %% "akka-stream"          % akka,
    "com.typesafe.akka" %% "akka-http"            % akka_http,
    "com.typesafe.akka" %% "akka-http-spray-json" % akka_http
  )
  val loggingDep = Seq(
    "com.typesafe.akka" %% "akka-slf4j"     % akka,
    "ch.qos.logback"    % "logback-classic" % logback
  )
  val sprayJsonDep = Seq(
    "io.spray" %% "spray-json" % spray_json
  )
  val pureConfigDep = Seq(
    "com.github.pureconfig" %% "pureconfig" % pure_config
  )
  val testingDep = Seq(
    "org.scalatest" %% "scalatest" % scala_test % Test,
    "org.scalamock" %% "scalamock" % scala_mock % Test
  )
  val scalazDep = Seq(
    "org.scalaz" %% "scalaz-core"       % scalaz_ver,
    "org.scalaz" %% "scalaz-concurrent" % scalaz_ver,
    "org.scalaz" %% "scalaz-effect"     % scalaz_ver
  )
  val doobieDep = Seq(
    "mysql"        % "mysql-connector-java" % mysql,
    "org.tpolecat" %% "doobie-core"         % doobie_ver,
    "org.tpolecat" %% "doobie-hikari"       % doobie_ver
  )

}
