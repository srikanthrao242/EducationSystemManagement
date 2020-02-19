/**/
package com.ems.student.examinations

import cats.Show
import com.ems.utilities.database.DBUtil.Dao
import com.ems.utilities.database.DBUtil.Dao.Aux
import com.ems.utilities.examination._

trait StudentMarksServices {
  implicit val student_MarksDao: Dao.Aux[Student_Marks, Int] =
    Dao.derive[Student_Marks, Int]("student_marks", "ID")
  implicit val student_MarksShow: Show[Student_Marks] = Show.fromToString
  val student_MarksDn: Aux[Student_Marks, Int] = Dao[Student_Marks]
  import student_MarksDn._
}
