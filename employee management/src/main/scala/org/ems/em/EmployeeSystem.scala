/**/
package org.ems.em

import akka.actor.Actor
import akka.event.slf4j.SLF4JLogging
import org.ems.em.entities._
import org.ems.em.service._
import akka.pattern.pipe
import org.ems.em.config.EmployeeConfig

class EmployeeSystem
  extends Actor
  with SalaryService
  with BankServices
  with EmployeeService
  with SLF4JLogging {

  implicit val ec = context.system.dispatcher


  // format: off
  override def receive: Receive = {

    case GetAllEmployeesProfile(userId) =>
      log.info(s"Got message to get all employees from userId $userId")
      getAllEmployeesProfiles(getDB(userId)).unsafeToFuture().pipeTo(sender)(self)
    case AddEmployeeProfile(userId, employee) =>
      log.info(s"Got message to add employee $employee from user $userId")
      addEmployeeProfile(getDB(userId),employee).unsafeToFuture().pipeTo(sender)(self)
    case DeleteEmployeeProfile(userId, id) =>
      log.info(s"Got message to delete employee $id from user $userId")
      deleteEmployeeProfile(getDB(userId),id).unsafeToFuture().pipeTo(sender)(self)
    case UpdateEmployeeProfile(userId, employee) =>
      log.info(s"Got message to update employee $employee from userId")
      updateEmployeeProfile(getDB(userId),employee).unsafeToFuture().pipeTo(sender)(self)

    case GetAllSalaries(userId) =>
      log.info(s"Got message to get all salaries from $userId")
      getAllSalaries(getDB(userId)).unsafeToFuture().pipeTo(sender)(self)
    case AddEmployeeSalary(userId, sal)=>
      log.info(s"Got message to add salary $sal from user $userId")
      addSalary(getDB(userId),sal).unsafeToFuture().pipeTo(sender)(self)
    case GetEmployeeSalary(userId, id) =>
      log.info(s"Got message to get an employee salary details with id: $id user $userId")
      sender ! getEmployeeSalary(getDB(userId),id).unsafeToFuture()
    case UpdateSalary(userId, salary) =>
      log.info(s"Got message to update the salary of employee $salary from user $userId")
      sender ! updateSalary(getDB(userId),salary).unsafeToFuture()
    case DeleteSalary(userId, id) =>
      log.info(s"Got message to delete salary of id : $id from user $userId")
      sender ! deleteSalary(getDB(userId),id).unsafeToFuture()

    case GetAllBankDetails(userId) =>
      log.info(s"Got message to get the all bank details from $userId")
      getAllBankDetails(getDB(userId)).unsafeToFuture().pipeTo(sender)(self)
    case GetBankDetails(userId, id) =>
      log.info(s"Got message to get the bank details of id : $id from user $userId")
      sender ! getEmployeeBankDetails(getDB(userId),id).unsafeToFuture()
    case UpdateBankDetails(userId, bankDetails) =>
      log.info(s"Got message to update the bank details $bankDetails from user $userId")
      sender ! updateBankDetails(getDB(userId),bankDetails).unsafeToFuture()
    case DeleteBankDetails(userId, id) =>
      log.info(s"Got message to delete bank details $id from user $userId")
      sender ! deleteBankDetails(getDB(userId),id).unsafeToFuture()
  }

  def getDB(userId: Int) : String = s"${EmployeeConfig.config.constants.db_prefix}_$userId"
}
