/**/
package org.ems.util

import akka.http.scaladsl.model._
import akka.http.scaladsl.model.headers._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server._
import HttpMethods._

trait CORSHandler{
  private val corsResponseHeaders = List(
    `Access-Control-Allow-Origin`.*,
    `Access-Control-Allow-Credentials`(true),
    `Access-Control-Allow-Headers`("Authorization",
      "Content-Type", "X-Requested-With")
  )
  //this directive adds access control headers to normal responses
  private def addAccessControlHeaders: Directive0 = {
    respondWithHeaders(corsResponseHeaders)
  }
  //this handles preflight OPTIONS requests.
  private def preflightRequestHandler: Route = options {
    complete(HttpResponse(StatusCodes.OK).
      withHeaders(`Access-Control-Allow-Methods`(OPTIONS, POST, PUT, GET, DELETE)))
  }
  // Wrap the Route with this method to enable adding of CORS headers
  def corsHandler(r: Route): Route = addAccessControlHeaders {
    preflightRequestHandler ~ r
  }
  // Helper method to add CORS headers to HttpResponse
  // preventing duplication of CORS headers across code
  def addCORSHeaders(response: HttpResponse):HttpResponse =
    response.withHeaders(corsResponseHeaders)
}
