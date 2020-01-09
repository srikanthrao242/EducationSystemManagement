/**/
package org.ems.em.routes

import java.nio.file.Paths

import akka.actor.{ActorSystem, Props}
import akka.event.slf4j.SLF4JLogging
import akka.http.scaladsl.server.Directives.{path, _}
import akka.http.scaladsl.server.Route
import org.ems.em.database.DbModule
import org.ems.em.entities._
import org.ems.em.service._

import scala.concurrent.ExecutionContext
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.{ContentType, ContentTypes, HttpEntity, HttpResponse, MediaTypes}
import org.ems.em.entities.EmployeesSer._
import spray.json.DefaultJsonProtocol._
import akka.pattern.ask
import akka.stream.scaladsl.FileIO
import akka.util.Timeout

import scala.concurrent.duration._
trait EmployeeRoutes extends SLF4JLogging with EmployeeService {

  implicit val executor: ExecutionContext
  implicit val actorSystem: ActorSystem
  def contentType(fileName: String): ContentType =
    (fileName.split("\\.").last) match {
      case "jpg"  => ContentType(MediaTypes.`image/jpeg`)
      case "png"  => ContentType(MediaTypes.`image/png`)
      case "html" => ContentTypes.`text/html(UTF-8)`
    }
  val addActor = actorSystem.actorOf(Props[AddEmployeeActor], "AddEmp")

  val employeesRoute: Route = pathPrefix("employees" / IntNumber) { userId =>
    concat(path("employee" / "image" / IntNumber) { id =>
      get {
        log.debug(s"Got request to get image ")
        complete(getEmployeeProImage(DbModule.getDB(userId), id).map {
          case Some(file) =>
            val path = System
              .getProperty("user.dir") + s"/Images/employeeimages/$file"
            val imageStream = FileIO.fromPath(Paths.get(path))
            val entity = HttpEntity(contentType(path), imageStream)
            log.debug("About to return stream...")
            HttpResponse(entity = entity)
        })
      }
    }) ~
    concat(path("employee" / IntNumber) { id =>
      get {
        log.debug(s"Got message to get employee details $id")
        complete(
          getEmployeeProfile(DbModule.getDB(userId), id).unsafeToFuture()
        )
      }
    }) ~
    delete {
      path(IntNumber) { id =>
        log.debug(s"Got Request to delete employee $id from $userId")
        complete(
          deleteEmployeeProfile(DbModule.getDB(userId), id)
            .unsafeToFuture()
            .map(_.toString)
        )
      }
    } ~
    path("add-employee") {
      post {
        entity(as[EmployeePSB]) { emp =>
          log.debug(s"Got Request to add employee $emp")
          implicit val timeout = Timeout(100 seconds)
          val res = addActor ? AddEmployee(userId,
                                           emp.employee,
                                           emp.salary,
                                           emp.bankDetails)
          complete(res.mapTo[MessageBack])
        }
      }
    } ~
    post {
      entity(as[Employee]) { emp =>
        log.debug(s"Got Request to add employee $emp")
        complete(
          addEmployeeProfile(DbModule.getDB(userId), emp)
            .unsafeToFuture()
            .map(_.toString)
        )
      }
    } ~
    get {
      log.debug(s"Got Request to get all Employees")
      complete(
        getAllEmployeesProfiles(DbModule.getDB(userId)).unsafeToFuture()
      )
    } ~
    put {
      entity(as[Employee]) { emp =>
        log.debug(s"Got Request to put employee details $emp")
        complete(
          updateEmployeeProfile(DbModule.getDB(userId), emp)
            .unsafeToFuture()
            .map(_.toString)
        )
      }
    }
  }
}
