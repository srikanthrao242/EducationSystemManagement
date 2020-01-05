/**/
package org.ems.entityroutes

import akka.event.slf4j.SLF4JLogging
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{Route, RouteConcatenation}
import org.ems.AkkaCoreModule
import org.ems.entities.RegisterUser
import org.ems.services.RegisterService
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import org.ems.entities.RegisterUserSer._
import spray.json._
import DefaultJsonProtocol._

trait Register
  extends RouteConcatenation
  with SLF4JLogging{
  this: AkkaCoreModule =>
  val registerService = new RegisterService(executor)

  val registerRoute: Route = {
    pathPrefix("users") {
      path("register") {
        post {
          entity(as[RegisterUser]) { reg =>
            complete(registerService.registerUser(reg))
          }
        }
      }
    }
  }
}
