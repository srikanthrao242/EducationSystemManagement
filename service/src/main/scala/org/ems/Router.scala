/**/
package org.ems

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{Route, RouteConcatenation}
import org.ems.cm.CompanyRoutes
import org.ems.config.ESMConfig
import org.ems.em.routes._
import org.ems.entityroutes.Register
import org.ems.um.UserRoutes
import org.ems.util.CORSHandler
import org.ems.util.ExceptionHandling._
trait Router
  extends RouteConcatenation
  with CompanyRoutes
  with UserRoutes
  with Register
  with EmployeeRoutes
  with SalaryRoutes
  with BankRoutes
  with CORSHandler {
  this: AkkaCoreModule =>
  val client = ESMConfig.config.client
  val mainRoute: Route =
    corsHandler {
      handleExceptions(exceptionHandler) {
        handleRejections(rejectionHandler) {
          pathSingleSlash {
            complete(StatusCodes.OK)
          } ~ pathPrefix("api") {
            companyRoute ~ registerRoute ~ userRoute ~ employeesRoute ~ salaryRoute ~ bankRoute
          }
        }
      }
    }
}
