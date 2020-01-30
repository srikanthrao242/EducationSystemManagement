/**/
package com.ems.student.student_details

import cats.Show
import com.ems.student.database.DbModule
import com.ems.utilities.database.DBUtil.Dao
import com.ems.utilities.database.DBUtil.Dao.Aux
import com.ems.utilities.student.entities._
import doobie._
import doobie.implicits._

import scala.concurrent.Future
trait AcademicStudentService {

  implicit val academicStudentDao: Dao.Aux[AcademicStudent, Int] =
    Dao.derive[AcademicStudent, Int]("students", "ID")
  implicit val academicStudentShow: Show[AcademicStudent] = Show.fromToString
  val academicStudentDn: Aux[AcademicStudent, Int] = Dao[AcademicStudent]
  import academicStudentDn._

  def addAcademicStudent(student:AcademicStudent, db:String): Future[Int] ={
    DbModule.transactor.use{xa=>
      insert(student,db).transact(xa)
    }.unsafeToFuture()
  }

  def deleteAcademicStudent(id:Int,db:String): Future[Int] ={
    DbModule.transactor.use{xa=>
      delete(id, db).transact(xa)
    }.unsafeToFuture()
  }

}
