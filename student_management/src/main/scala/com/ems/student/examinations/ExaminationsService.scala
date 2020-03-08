/**/
package com.ems.student.examinations

import cats.Show
import com.ems.student.database.DbModule
import com.ems.utilities.database.DBUtil.Dao
import com.ems.utilities.database.DBUtil.Dao.Aux
import com.ems.utilities.examination._
import doobie.implicits._

import scala.concurrent.Future

trait ExaminationsService {

  implicit val examinations_detailsDao: Dao.Aux[Examinations_Details, Int] =
    Dao.derive[Examinations_Details, Int]("examinations", "ExamID")
  implicit val examinations_detailsShow: Show[Examinations_Details] = Show.fromToString
  val examinations_detailsDn: Aux[Examinations_Details, Int] = Dao[Examinations_Details]
  import examinations_detailsDn._

  def addExam(exam: Examinations_Details, db: String): Future[Int] ={
    DbModule.transactor.use{xa=>
      insert(exam,db).transact(xa)
    }.unsafeToFuture()
  }

  def getExams(academicId:Int,db:String):Future[List[Examinations_Details]]={
    DbModule.transactor.use{xa=>
      findAllBy(academicId, "AcademicID", db).transact(xa).compile.toList
    }.unsafeToFuture()
  }


}
