/**/
package org.ems.entityroutes

import akka.actor.Props
import akka.event.slf4j.SLF4JLogging
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{Route, RouteConcatenation}
import org.ems.AkkaCoreModule
import org.ems.services.UserService
import org.ems.um.UserManagementSystem
import spray.json._
import DefaultJsonProtocol._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import org.ems.um.entities.User
import org.ems.um.entities.UserSer._
trait Users extends RouteConcatenation with SLF4JLogging {
  this: AkkaCoreModule =>
  val userActor = actorSystem.actorOf(Props[UserManagementSystem], "user")
  val userService = new UserService(userActor)

  val userRoute : Route = {
    pathPrefix("users") {
      path(IntNumber) { id =>
        get {
          log.debug(s"Got get request for id $id")
          complete(userService.getUser(id).map(v => v))
        } ~
          delete {
            log.debug(s"Got request for delete for id $id")
            complete(userService.deleteUser(id).map(v => v.toString))
          }
      } ~
        post {
          entity(as[User]) { comp: User =>
            log.debug(s"Got request for insert $comp")
            complete(userService.addUser(comp).map(_.toString()))
          }
        } ~
        put {
          entity(as[User]) { comp: User =>
            log.debug(s"Got request for put for $comp")
            complete(userService.updateUser(comp).map(_.toString()))
          }
        } ~
        get {
          log.debug(s"Got request for get for Users")
          complete(
            userService.getAllUsers
          )
        }
    }
  }

}
