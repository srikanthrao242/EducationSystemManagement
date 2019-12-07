package org.ems

import akka.event.Logging
import akka.http.scaladsl.Http
import org.ems.config.ESMConfig


trait WebServer extends {
  this: AkkaCoreModule
    with Router =>

  val log = Logging(actorSystem, this.getClass.getName)

  private val host = ESMConfig.config.http.host
  private val port = ESMConfig.config.http.port

  private val binding = Http().bindAndHandle(mainRoute, host, port)
  log.info(s"server listening on port $port")

}
