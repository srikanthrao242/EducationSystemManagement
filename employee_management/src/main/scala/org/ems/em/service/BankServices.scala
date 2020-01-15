/**/
package org.ems.em.service
import cats.Show
import cats.effect.IO
import com.ems.utilities.database.DBUtil.Dao._
import com.ems.utilities.database.DBUtil._
import com.ems.utilities.employees.entities.BankDetails
import org.ems.em.database.DbModule
import doobie._
import doobie.implicits._
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
      find(id,db).transact(xa)
    }

  def updateBankDetails(db:String,bankDetails: BankDetails): IO[Int] =
    DbModule.transactor.use { xa =>
      bankDetails.id.fold {
        throw new Exception(s"Need id to update bank details")
      } { id =>
        update(id, bankDetails,db).transact(xa)
      }
    }

  def addEmployeeBankDetails(db:String,bankDetails: BankDetails): IO[Int] =
    DbModule.transactor.use { xa =>
      insert(bankDetails,db).transact(xa)
    }

  def deleteBankDetails(db:String,id: Int): IO[Int] = DbModule.transactor.use { xa =>
    delete(id,db).transact(xa)
  }

  def getBankByEmpId(db: String, empId: Int): IO[Option[BankDetails]] =
    DbModule.transactor.use { xa =>
      findBy(empId, "employeeId",db).transact(xa)
    }

}
