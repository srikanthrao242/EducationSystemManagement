/**/
package org.ems.em.routes

import akka.event.slf4j.SLF4JLogging
import akka.http.scaladsl.server.Directives._
import org.ems.em.database.DbModule
import org.ems.em.entities._
import org.ems.em.service.SalaryService
import org.ems.em.entities.EmployeesSer._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.server.Route
import spray.json.DefaultJsonProtocol._
import scala.concurrent.ExecutionContext

trait SalaryRoutes extends SLF4JLogging with SalaryService {

  implicit val executor: ExecutionContext

  val salaryRoute: Route = pathPrefix("salaries" / IntNumber) { userId =>
    concat(
      get {
        path("salary" / IntNumber) { id =>
          log.debug(s"Got request to get salary by employee id $id")
          complete(
            getSalaryByEmpId(DbModule.getDB(userId), id).unsafeToFuture()
          )
        } ~
        path(IntNumber) { id =>
          log.debug(s"Got Request to get employee salary $id")
          complete(
            getEmployeeSalary(DbModule.getDB(userId), id)
              .unsafeToFuture()
              .map(_.toString)
          )
        }
      }
    ) ~
    get {
      log.debug(s"Got Request to all employees salaries")
      complete(getAllSalaries(DbModule.getDB(userId)).unsafeToFuture())
    } ~
    post {
      entity(as[Salary]) { sal =>
        log.debug(s"Got request to add employee salary $sal")
        complete(
          addSalary(DbModule.getDB(userId), sal)
            .unsafeToFuture()
            .map(_.toString)
        )
      }
    } ~
    put {
      entity(as[Salary]) { sal =>
        log.debug(s"Got request to update employee salary $sal")
        complete(
          updateSalary(DbModule.getDB(userId), sal)
            .unsafeToFuture()
            .map(_.toString)
        )
      }
    }
  }
}
