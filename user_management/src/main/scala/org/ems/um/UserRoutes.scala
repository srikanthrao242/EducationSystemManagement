/**/
package org.ems.um

import akka.event.slf4j.SLF4JLogging
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import org.ems.um.entities.{Activation, Authenticate, User}
import org.ems.um.services.UserService
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import org.ems.um.entities.UserSer._
import spray.json.DefaultJsonProtocol._

trait UserRoutes extends SLF4JLogging with UserService {

  val userRoute: Route = {
    pathPrefix("users" / IntNumber) { id =>
      path("activation") {
        put {
          entity(as[Activation]) { act =>
            log.debug(s"Got request to do activation $act")
            complete(activation(id, act).map(_.toString))
          }
        }
      } ~
      concat(
        get {
          log.debug(s"Got get request for id $id")
          complete(getUser(id).map(v => v))
        } ~
        delete {
          log.debug(s"Got request for delete for id $id")
          complete(deleteUser(id).map(v => v.toString))
        }
      )
    } ~
    path("users" / "authenticate") {
      post {
        entity(as[Authenticate]) { auth =>
          log.debug(
            s"Got request to authenticate ${auth.email}  && ${auth.password}"
          )
          complete(authenticateUser(auth))
        }
      }
    } ~ path("users") {
      post {
        entity(as[User]) { comp: User =>
          log.debug(s"Got request for insert $comp")
          complete(addUser(comp).map(_.toString()))
        }
      } ~
      put {
        entity(as[User]) { comp: User =>
          log.debug(s"Got request for put for $comp")
          complete(updateUser(comp).map(_.toString()))
        }
      } ~
      get {
        log.debug(s"Got request for get for Users")
        complete(
          getAllUsers
        )
      }
    }
  }

}
