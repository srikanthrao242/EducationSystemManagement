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

  def getAllBankDetails: IO[List[BankDetails]] = DbModule.transactor.use { xa =>
    findAll.transact(xa).compile.toList
  }

  def getEmployeeBankDetails(id: Int): IO[Option[BankDetails]] =
    DbModule.transactor.use { xa =>
      find(id).transact(xa)
    }

  def updateBankDetails(bankDetails: BankDetails): IO[Int] =
    DbModule.transactor.use { xa =>
      bankDetails.id.fold {
        throw new Exception(s"Need id to update bank details")
      } { id =>
        update(id, bankDetails.copy(id = None)).transact(xa)
      }
    }

  def addEmployeeBankDetails(bankDetails: BankDetails): IO[Int] =
    DbModule.transactor.use { xa =>
      insert(bankDetails).transact(xa)
    }

  def deleteBankDetails(id: Int): IO[Int] = DbModule.transactor.use { xa =>
    delete(id).transact(xa)
  }

}
