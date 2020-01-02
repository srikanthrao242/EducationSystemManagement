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
import org.ems.em.service.InsertEmployee
import akka.pattern.ask
import akka.util.Timeout

import scala.concurrent.duration._

trait Employees extends RouteConcatenation with SLF4JLogging {
  this: AkkaCoreModule =>
  case class AddEmployeePSB(employee: Employee,
                            salary: Salary,
                            bankDetails: BankDetails)
  implicit val addEmpPsbf = jsonFormat3(AddEmployeePSB)
  implicit val messageBackf = jsonFormat3(MessageBack)
  val employeesActor = actorSystem.actorOf(Props[EmployeeSystem], "Employee")
  val employeeService = new EmployeeService(employeesActor)
  val addActor = actorSystem.actorOf(Props[AddEmployeeAct], "AddEmployee")
  implicit val timeout = Timeout(100 seconds)
  val employeesRoute = pathPrefix("employees" / IntNumber) { userId =>
    delete {
      path(IntNumber) { id =>
        log.debug(s"Got Request to delete employee $id from $userId")
        complete(
          employeeService
            .doProcess[DeleteEmployeeProfile, Int](
              DeleteEmployeeProfile(userId, id)
            )
            .map(_.toString)
        )
      }
    } ~
    path("add-employee") {
      post {
        entity(as[AddEmployeePSB]) { emp =>
          log.debug(s"Got Request to add employee $emp")
          val res = addActor ? AddEmployee(userId,
            emp.employee,
            emp.salary,
            emp.bankDetails)
          complete(res.mapTo[MessageBack])
        }
      }
    }~
    post {
      entity(as[Employee]) { emp =>
        log.debug(s"Got Request to add employee $emp")
        complete(
          employeeService
            .doProcess[AddEmployeeProfile, Int](AddEmployeeProfile(userId, emp))
            .map(_.toString)
        )
      }
    } ~
    get {
      log.debug(s"Got Request to get all Employees")
      complete(
        employeeService
          .doProcess[GetAllEmployeesProfile, List[Employee]](
            GetAllEmployeesProfile(userId)
          )
      )
    } ~
    put {
      entity(as[Employee]) { emp =>
        log.debug(s"Got Request to put employee details ")
        complete(
          employeeService
            .doProcess[UpdateEmployeeProfile, Int](
              UpdateEmployeeProfile(userId, emp)
            )
            .map(_.toString)
        )
      }
    }
  }
}
