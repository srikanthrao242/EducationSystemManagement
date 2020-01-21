/**/
package com.ems.student.admission_fee_details


import cats.Show
import cats.effect.IO
import com.ems.student.database.DbModule
import com.ems.utilities.database.DBUtil.Dao
import com.ems.utilities.database.DBUtil.Dao._
import com.ems.utilities.student.entities._
import doobie._
import doobie.implicits._

import scala.concurrent.Future

trait AdmissionFeeDetailsService {

  implicit val admissionFeeDao: Dao.Aux[AdmissionFeeDetails, Int] =
    Dao.derive[AdmissionFeeDetails, Int]("admission_fee_details", "ID")
  implicit val admissionFeeShow: Show[AdmissionFeeDetails] = Show.fromToString
  val admissionFeeDn: Aux[AdmissionFeeDetails, Int] = Dao[AdmissionFeeDetails]
  import admissionFeeDn._

  def addNewAdmissionFee(admissionFee: AdmissionFeeDetails, db: String): Future[Int] =
    DbModule.transactor.use { xa =>
      insert(admissionFee, db).transact(xa)
    }.unsafeToFuture()

}
