/**/
package com.ems.student.admission_details


import cats.Show
import cats.effect.IO
import com.ems.student.database.DbModule
import com.ems.utilities.database.DBUtil.Dao
import com.ems.utilities.database.DBUtil.Dao._
import com.ems.utilities.student.entities._
import doobie._
import doobie.implicits._

import scala.concurrent.Future

trait AdmissionDetailsService {

  implicit val admissionDetailsDao: Dao.Aux[Admission, Int] =
    Dao.derive[Admission, Int]("admission", "AdmissionID")
  implicit val admissionDetailsShow: Show[Admission] = Show.fromToString
  val admissionDetailsDn: Aux[Admission, Int] = Dao[Admission]
  import admissionDetailsDn._

  def addNewAdmission(admission: Admission, db: String): Future[Int] =
    DbModule.transactor.use { xa =>
      insert(admission, db).transact(xa)
    }.unsafeToFuture()

}
