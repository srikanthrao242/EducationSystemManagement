/**/
package com.ems.student.adminssion

import akka.event.slf4j.SLF4JLogging
import com.ems.student.class_details.ClassServices
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.server.Route
import com.ems.student.database.DbModule._
import com.ems.utilities.Mappable._
import spray.json.DefaultJsonProtocol._

import scala.concurrent.ExecutionContext
trait NewStudent extends SLF4JLogging with ClassServices with NewAdmissionServices{
  implicit val executor: ExecutionContext

  val newStudent: Route = pathPrefix("new-admission" / IntNumber){ userId=>
    post{
      entity(as[Map[String,Any]]){
        req=>
          complete(admitNewStudent(req, getDB(userId)).map(_.toString))
      }
    }
  }

}
