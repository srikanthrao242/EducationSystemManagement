/**/
package org.ems.entityroutes

import akka.actor.Props
import akka.event.slf4j.SLF4JLogging
import akka.http.scaladsl.server.RouteConcatenation
import org.ems.AkkaCoreModule
import org.ems.em.EmployeeSystem
import akka.http.scaladsl.server.Directives._
import org.ems.em.entities._
import org.ems.services.EmployeeService
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import spray.json._
import EmployeesSer._
import DefaultJsonProtocol._
trait Employees extends RouteConcatenation with SLF4JLogging {
  this: AkkaCoreModule =>
  val employeesActor = actorSystem.actorOf(Props[EmployeeSystem], "Employee")
  val employeeService = new EmployeeService(employeesActor)
  val employeesRoute = pathPrefix("employees") {
    delete {
      path(IntNumber) { id =>
        log.debug(s"Got Request to delete employee $id")
        complete(
          employeeService
            .doProcess[DeleteEmployeeProfile, Int](DeleteEmployeeProfile(id))
            .map(_.toString)
        )
      }
    } ~
    post {
      entity(as[Employee]) { emp =>
        log.debug(s"Got Request to add employee $emp")
        complete(
          employeeService
            .doProcess[AddEmployeeProfile, Int](AddEmployeeProfile(emp))
            .map(_.toString)
        )
      }
    } ~
    get {
      log.debug(s"Got Request to get all Employees")
      complete(
        employeeService
          .doProcess[GetAllEmployeesProfile.type, List[Employee]](
            GetAllEmployeesProfile
          )
      )
    } ~
    put {
      entity(as[Employee]) { emp =>
        log.debug(s"Got Request to put employee details ")
        complete(
          employeeService
            .doProcess[UpdateEmployeeProfile, Int](UpdateEmployeeProfile(emp))
            .map(_.toString)
        )
      }
    }
  }
}
