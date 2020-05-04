/**/
package com.ems.student.examinations

import cats.Show
import com.ems.student.database.DbModule
import com.ems.utilities.database.DBUtil.Dao
import com.ems.utilities.database.DBUtil.Dao.Aux
import com.ems.utilities.examination._
import doobie._
import doobie.implicits._
import scala.concurrent.Future

trait ExamSubjectServices {
  implicit val examinations_subjectsDao: Dao.Aux[Examinations_Subjects, Int] =
    Dao.derive[Examinations_Subjects, Int]("examination_subjects", "ExamID")
  implicit val examinations_subjectsShow: Show[Examinations_Subjects] =
    Show.fromToString
  val examinations_subjectsDn: Aux[Examinations_Subjects, Int] =
    Dao[Examinations_Subjects]
  import examinations_subjectsDn._

  def getExamSubjectByExamID(examId: Int, db: String): Future[List[Examinations_Subjects]] ={
    DbModule.transactor.use{xa=>
      findAllBy(examId,"ExamID", db).transact(xa).compile.toList
    }.unsafeToFuture()
  }

  def addSubject(subject: Examinations_Subjects, db: String): Future[Int] ={
    DbModule.transactor.use{xa=>
      insert(subject, db).transact(xa)
    }.unsafeToFuture()
  }

  def deleteSubject(subjectID:Int, db:String): Future[Int] ={
    DbModule.transactor.use{xa=>
      deleteBy(subjectID, "SubjectID", db).transact(xa)
    }.unsafeToFuture()
  }

}
