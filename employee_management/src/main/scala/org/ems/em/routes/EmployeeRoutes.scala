/**/
package org.ems.em.routes


import akka.actor.{ActorSystem, Props}
import akka.event.slf4j.SLF4JLogging
import akka.http.scaladsl.server.Directives.{path, _}
import akka.http.scaladsl.server.Route
import org.ems.em.database.DbModule
import org.ems.em.service._

import scala.concurrent.ExecutionContext
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import com.ems.utilities.employees.entities.EmployeesSer._
import spray.json.DefaultJsonProtocol._
import akka.pattern.ask
import akka.util.Timeout
import com.ems.utilities.employees.entities._
import com.ems.utilities.fileUtils.FileResponseHelper._

import scala.concurrent.duration._
trait EmployeeRoutes extends SLF4JLogging with EmployeeService {

  implicit val executor: ExecutionContext
  implicit val actorSystem: ActorSystem

  val addActor = actorSystem.actorOf(Props[AddEmployeeActor], "AddEmp")

  val employeesRoute: Route = pathPrefix("employees" / IntNumber) { userId =>
    concat(path("employee" / "image" / IntNumber) { id =>
      get {
        log.debug(s"Got request to get image ")
        complete(getEmployeeProImage(DbModule.getDB(userId), id).map {
          case Some(file) =>
            completeFile("employeeimages", file)
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
