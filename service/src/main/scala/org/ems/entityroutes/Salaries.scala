/**/
package org.ems.entityroutes

import akka.actor.Props
import akka.event.slf4j.SLF4JLogging
import akka.http.scaladsl.server.RouteConcatenation
import org.ems.AkkaCoreModule
import org.ems.em.EmployeeSystem
import org.ems.services.SalaryServices
import akka.http.scaladsl.server.Directives._
import org.ems.em.entities._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import spray.json._
import EmployeesSer._
import DefaultJsonProtocol._

trait Salaries extends RouteConcatenation with SLF4JLogging {
  this: AkkaCoreModule =>

  val salaryActor = actorSystem.actorOf(Props[EmployeeSystem], "Salary")
  val salaryServices = new SalaryServices(salaryActor)

  val salaryRoute = pathPrefix("salaries") {
    get {
      log.debug(s"Got Request to all employees salaries")
      complete(salaryServices.doProcess[_, List[Salary]](GetAllSalaries))
    } ~
    get {
      path(IntNumber) { id =>
        log.debug(s"Got Request to get employee salary $id")
        complete(
          salaryServices.doProcess[_, Option[Salary]](GetEmployeeSalary(id))
        )
      }
    } ~
    post {
      entity(as[Salary]) { sal =>
        log.debug(s"Got request to add employee salary $sal")
        complete(
          salaryServices
            .doProcess[_, Int](AddEmployeeSalary(sal))
            .map(_.toString)
        )
      }
    } ~
    put {
      entity(as[Salary]) { sal =>
        log.debug(s"Got request to update employee salary $sal")
        complete(
          salaryServices.doProcess[_, Int](UpdateSalary(sal)).map(_.toString)
        )
      }
    }
  }
}
