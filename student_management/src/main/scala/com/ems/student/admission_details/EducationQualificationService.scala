/**/
package com.ems.student.admission_details

import cats.Show
import com.ems.student.database.DbModule
import com.ems.utilities.database.DBUtil.Dao
import com.ems.utilities.database.DBUtil.Dao.Aux
import com.ems.utilities.student.entities._
import doobie._
import doobie.implicits._
import scala.concurrent.Future

trait EducationQualificationService {
  implicit val educationQualDetailsDao: Dao.Aux[EducationQualification, Int] =
    Dao.derive[EducationQualification, Int]("education_qualification", "EdID")
  implicit val educationQualShow: Show[EducationQualification] = Show.fromToString
  val educationQualDn: Aux[EducationQualification, Int] = Dao[EducationQualification]
  import educationQualDn._

  def addEdQual(edQual : EducationQualification, db:String): Future[Int] ={
    DbModule.transactor.use{xa=>
      insert(edQual,db).transact(xa)
    }.unsafeToFuture()
  }
}
