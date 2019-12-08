/**/
package org.ems.util
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server._
import akka.http.scaladsl.model._

object ExceptionHandling {

  implicit val rejectionHandler: RejectionHandler =
    RejectionHandler.default.mapRejectionResponse {
      case res @ HttpResponse(_, _, ent: HttpEntity.Strict, _) =>
        val message = ent.data.utf8String.replaceAll("\"", """\"""")
        res.copy(
          entity = message
        )
      case x => x
    }
  implicit val exceptionHandler: ExceptionHandler =
    ExceptionHandler {
      case e: Exception =>
        e.printStackTrace()
        complete(
          HttpResponse(
            StatusCodes.BadRequest,
            Nil,
            e.getMessage
          )
        )
    }

}
