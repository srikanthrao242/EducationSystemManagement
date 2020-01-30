/**/
package com.ems.student.student_details

import akka.event.slf4j.SLF4JLogging
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.server.Route
import com.ems.student.database.DbModule
import com.ems.utilities.student.entities.ListStudentRequest
import com.ems.utilities.student.entities.StudentSer._
import scala.concurrent.ExecutionContext
import spray.json.DefaultJsonProtocol._

trait StudentDetailsRoute extends SLF4JLogging with StudentDetailsService {
  implicit val executor: ExecutionContext

  val studentDetailsRoute: Route = pathPrefix("student-details" / IntNumber) {
    userID =>
      path("students") {
        post {
          entity(as[ListStudentRequest]) { req =>
            complete(getStudentList(req, DbModule.getDB(userID)))
          }
        }
      }
  }

}
