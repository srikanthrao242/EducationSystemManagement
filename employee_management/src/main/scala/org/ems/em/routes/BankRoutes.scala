/**/
package org.ems.em.routes

import akka.event.slf4j.SLF4JLogging
import akka.http.scaladsl.server.Directives._
import com.ems.utilities.employees.entities._
import org.ems.em.service.BankServices
import org.ems.em.database.DbModule._
import com.ems.utilities.employees.entities.EmployeesSer._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.server.Route
import spray.json.DefaultJsonProtocol._
import scala.concurrent.ExecutionContext

trait BankRoutes extends SLF4JLogging with BankServices {
  implicit val executor: ExecutionContext
  val bankRoute: Route = pathPrefix("banks" / IntNumber) { userId =>
    concat(get {
      path("bank" / IntNumber) { id =>
        log.debug(s"Got request to get the bank details by id $id")
        complete(getBankByEmpId(getDB(userId), id).unsafeToFuture())
      }
    }) ~
    get {
      log.debug(s"Got request to get all banks details")
      complete(
        getAllBankDetails(getDB(userId)).unsafeToFuture()
      )
    } ~
    path(IntNumber) { id =>
      get {
        log.debug(s"Got request to get banks details by id $id")
        complete(
          getEmployeeBankDetails(getDB(userId), id).unsafeToFuture()
        )
      } ~
      delete {
        log.debug(s"Got request to delete bank details of id $id")
        complete(
          deleteBankDetails(getDB(userId), id).unsafeToFuture().map(_.toString)
        )
      }
    } ~ post {
      entity(as[BankDetails]) { bank =>
        log.debug(s"Got request to add bank details $bank")
        complete(
          addEmployeeBankDetails(getDB(userId), bank)
            .unsafeToFuture()
            .map(_.toString)
        )
      }
    } ~ put {
      entity(as[BankDetails]) { bank =>
        log.debug(s"Got request to update the bank details $bank")
        complete(
          updateBankDetails(getDB(userId), bank)
            .unsafeToFuture()
            .map(_.toString)
        )
      }
    }
  }
}
