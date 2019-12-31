/**/
package org.ems.em.service

import cats.Show
import cats.effect.IO
import org.ems.em.database.DBUtil._
import org.ems.em.database.DbModule
import org.ems.em.entities._
import doobie._
import doobie.implicits._
import org.ems.em.database.DBUtil.Dao.Aux

trait EmployeeService {
  implicit val empDao: Dao.Aux[Employee, Int] =
    Dao.derive[Employee, Int]("employees", "id")
  implicit val empShow: Show[Employee] = Show.fromToString
  val empDn: Aux[Employee, Int] = Dao[Employee]
  import empDn._

  def getAllEmployeesProfiles(db:String): IO[List[Employee]] = DbModule.transactor.use {
    xa =>
      findAll(db).transact(xa).compile.toList
  }

  def getEmployeeProfile(db:String, id: Int): IO[Option[Employee]] =
    DbModule.transactor.use { xa =>
      find(db,id).transact(xa)
    }

  def deleteEmployeeProfile(db:String, id: Int): IO[Int] = DbModule.transactor.use { xa =>
    delete(db,id).transact(xa)
  }

  def updateEmployeeProfile(db:String, employee: Employee): IO[Int] =
    DbModule.transactor.use { xa =>
      employee.id
        .fold(throw new Exception("Employee Id is mandatory to update"))(id => {
          update(db,id, employee.copy(id = None)).transact(xa)
        })
    }

  def addEmployeeProfile(db:String, employee: Employee): IO[Int] = DbModule.transactor.use { xa =>
    insert(db,employee).transact(xa)
  }

}
