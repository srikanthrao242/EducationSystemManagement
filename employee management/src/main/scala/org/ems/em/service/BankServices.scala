/**/
package org.ems.em.service
import cats.Show
import cats.effect.IO
import org.ems.em.database.DbModule
import org.ems.em.entities._
import doobie._
import doobie.implicits._
import org.ems.em.database.DBUtil.Dao
import org.ems.em.database.DBUtil.Dao.Aux
trait BankServices {
  implicit val bankDao: Dao.Aux[BankDetails, Int] =
    Dao.derive[BankDetails, Int]("bank_details", "id")
  implicit val bankShow: Show[BankDetails] = Show.fromToString
  val bankDn: Aux[BankDetails, Int] = Dao[BankDetails]
  import bankDn._

  def getAllBankDetails(db:String): IO[List[BankDetails]] = DbModule.transactor.use { xa =>
    findAll(db).transact(xa).compile.toList
  }

  def getEmployeeBankDetails(db:String,id: Int): IO[Option[BankDetails]] =
    DbModule.transactor.use { xa =>
      find(db,id).transact(xa)
    }

  def updateBankDetails(db:String,bankDetails: BankDetails): IO[Int] =
    DbModule.transactor.use { xa =>
      bankDetails.id.fold {
        throw new Exception(s"Need id to update bank details")
      } { id =>
        update(db,id, bankDetails).transact(xa)
      }
    }

  def addEmployeeBankDetails(db:String,bankDetails: BankDetails): IO[Int] =
    DbModule.transactor.use { xa =>
      insert(db,bankDetails).transact(xa)
    }

  def deleteBankDetails(db:String,id: Int): IO[Int] = DbModule.transactor.use { xa =>
    delete(db,id).transact(xa)
  }

  def getBankByEmpId(db: String, empId: Int): IO[Option[BankDetails]] =
    DbModule.transactor.use { xa =>
      findBy(db, empId, "employeeId").transact(xa)
    }

}
