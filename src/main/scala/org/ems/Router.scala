/**/
package org.ems

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server._
import org.ems.util.ExceptionHandling._

trait Router extends RouteConcatenation {
  this: AkkaCoreModule =>
  val mainRoute: Route =
    handleExceptions(exceptionHandler) {
      handleRejections(rejectionHandler) {
        pathSingleSlash {
          complete(StatusCodes.OK)
        }
      }
    }
}
