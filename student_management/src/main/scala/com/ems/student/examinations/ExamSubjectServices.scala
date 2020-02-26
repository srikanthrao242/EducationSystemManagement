/**/
package com.ems.student.examinations

import cats.Show
import com.ems.utilities.database.DBUtil.Dao
import com.ems.utilities.database.DBUtil.Dao.Aux
import com.ems.utilities.examination._

trait ExamSubjectServices {
  implicit val examinations_subjectsDao: Dao.Aux[Examinations_Subjects, Int] =
    Dao.derive[Examinations_Subjects, Int]("examination_subjects", "ExamID")
  implicit val examinations_subjectsShow: Show[Examinations_Subjects] =
    Show.fromToString
  val examinations_subjectsDn: Aux[Examinations_Subjects, Int] =
    Dao[Examinations_Subjects]
  import examinations_subjectsDn._
}
