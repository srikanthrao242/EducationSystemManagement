/**/
package com.ems.student.class_details

import akka.event.slf4j.SLF4JLogging
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.server.Route
import com.ems.student.database.DbModule
import com.ems.utilities.student.entities._
import com.ems.utilities.student.entities.StudentSer._
import spray.json.DefaultJsonProtocol._

import scala.concurrent.ExecutionContext

trait ClassesRoute extends SLF4JLogging with ClassServices {
  implicit val executor: ExecutionContext

  val classesRoute: Route = pathPrefix("classes" / "sections" / IntNumber) {
    userId =>
      post {
        entity(as[List[ClassCreateRequest]]) { req =>
          complete(createClassesAndSections(req, DbModule.getDB(userId)))
        }
      } ~ path(IntNumber) { academicId =>
        get {
          complete(getClassesAndSections(academicId, DbModule.getDB(userId)))
        }
      }
  }

}
