/**/
package org.ems

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives.{complete, handleExceptions, handleRejections, pathPrefix, pathSingleSlash}
import akka.http.scaladsl.server.{Route, RouteConcatenation}
import org.ems.config.ESMConfig
import org.ems.entityroutes.{Companies, Register, Users}
import org.ems.util.CORSHandler
import org.ems.util.ExceptionHandling._
trait Router
  extends RouteConcatenation
  with Companies
  with Users
  with Register
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
            companyRoute  ~ registerRoute ~ userRoute
          }
        }
      }
    }
}
