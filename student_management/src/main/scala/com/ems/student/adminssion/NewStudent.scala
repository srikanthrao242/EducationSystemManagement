/**/
package com.ems.student.adminssion

import cats.Show
import com.ems.utilities.database.DBUtil.Dao
import com.ems.utilities.database.DBUtil.Dao._
import com.ems.utilities.student.entities._

class NewStudent {

  implicit val studentDao: Dao.Aux[Student, Int] =
    Dao.derive[Student, Int]("student_details", "id")
  implicit val studentShow: Show[Student] = Show.fromToString
  val studentDn: Aux[Student, Int] = Dao[Student]
  import studentDn._

  def add(student:Student, academicYear:String, employeeId:Int): Unit ={
    //insert(student,)
  }
}
