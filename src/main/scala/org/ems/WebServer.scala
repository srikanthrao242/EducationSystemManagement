/**/
package org.ems

import akka.event.slf4j.SLF4JLogging
import akka.http.scaladsl.Http
import org.ems.config.ESMConfig

import scala.util.{Failure, Success}


trait WebServer  extends SLF4JLogging{
  this: AkkaCoreModule
    with Router =>

  private val host = ESMConfig.config.http.host
  private val port = ESMConfig.config.http.port

  private val binding = Http().bindAndHandle(mainRoute, host, port)
  binding.onComplete{
    case Success(_) =>
      log.info(s"server listening on port $port")
    case Failure(exception) =>
    log.debug(s"Got error while starting server",exception)
  }
}
