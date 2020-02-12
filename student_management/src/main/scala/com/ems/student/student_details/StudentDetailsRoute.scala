/**/
package com.ems.student.student_details

import akka.event.slf4j.SLF4JLogging
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.server.Route
import com.ems.student.database.DbModule
import com.ems.utilities.fileUtils.FileResponseHelper.completeFile
import com.ems.utilities.student.entities.ListStudentRequest
import com.ems.utilities.student.entities.StudentSer._

import scala.concurrent.ExecutionContext
import spray.json.DefaultJsonProtocol._

trait StudentDetailsRoute extends SLF4JLogging with StudentDetailsService {
  implicit val executor: ExecutionContext

  val studentDetailsRoute: Route = pathPrefix("student-details" / IntNumber) {
    userID =>
      path("students") {
        parameter('academicID.as[Int], 'classID.as[Int], 'sectionID.as[Int])
          .as(ListStudentRequest) { req =>
            get {
              complete(getStudentList(req, DbModule.getDB(userID)))
            }
          }
      } ~ path("profile" / IntNumber) { stdID =>
        get {
          complete(getStudentImage(stdID, DbModule.getDB(userID)).map {
            case Some(file) =>
              completeFile("studentimages", file)})
        }
      }
  }

}
