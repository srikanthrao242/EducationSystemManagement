import sbt._
object Dependencies {

  val serviceMod = "service"
  val companyMgtMod = "company management"

  val akka = "2.6.1"
  val akka_http = "10.1.11"
  val logback = "1.2.3"
  val spray_json = "1.3.5"
  val pure_config = "0.12.1"
  val scala_test = "3.0.8"
  val scala_mock = "4.4.0"

  val akkaDep = Seq(
    "com.typesafe.akka" %% "akka-actor"     % akka,
    "com.typesafe.akka" %% "akka-stream"    % akka,
    "com.typesafe.akka" %% "akka-http"      % akka_http,
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

}
