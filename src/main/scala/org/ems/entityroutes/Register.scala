/**/
package org.ems.entityroutes

import akka.event.slf4j.SLF4JLogging
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.RouteConcatenation
import org.ems.AkkaCoreModule
import org.ems.entities.RegisterUser
import org.ems.services.RegisterService
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import org.ems.entities.RegisterUserSer._
import spray.json._
import DefaultJsonProtocol._
trait Register
  extends RouteConcatenation
  with SLF4JLogging
  with Users
  with Companies {
  this: AkkaCoreModule =>
  val registerService = new RegisterService(userService, companyService)

  val registerRoute = {
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
