/**/
package org.ems.em

import akka.actor.Actor
import akka.event.slf4j.SLF4JLogging
import org.ems.em.entities._
import org.ems.em.service._
import akka.pattern.pipe

class EmployeeSystem
  extends Actor
  with SalaryService
  with BankServices
  with EmployeeService
  with SLF4JLogging {
  implicit val ec = context.system.dispatcher


  // format: off
  override def receive: Receive = {

    case GetAllEmployeesProfile =>
      log.info(s"Got message to get all employees")
      getAllEmployeesProfiles.unsafeToFuture().pipeTo(sender)(self)
    case AddEmployeeProfile(employee) =>
      log.info(s"Got message to add employee $employee")
      addEmployeeProfile(employee).unsafeToFuture().pipeTo(sender)(self)
    case DeleteEmployeeProfile(id) =>
      log.info(s"Got message to delete employee $id")
      deleteEmployeeProfile(id).unsafeToFuture().pipeTo(sender)(self)
    case UpdateEmployeeProfile(employee) =>
      log.info(s"Got message to update employee $employee")
      updateEmployeeProfile(employee).unsafeToFuture().pipeTo(sender)(self)

    case GetAllSalaries =>
      log.info(s"Got message to get all salaries")
      getAllSalaries.unsafeToFuture().pipeTo(sender)(self)
    case AddEmployeeSalary(sal)=>
      log.info(s"Got message to add salary $sal")
      addSalary(sal).unsafeToFuture().pipeTo(sender)(self)
    case GetEmployeeSalary(id) =>
      log.info(s"Got message to get an employee salary details with id: $id")
      sender ! getEmployeeSalary(id).unsafeToFuture()
    case UpdateSalary(salary) =>
      log.info(s"Got message to update the salary of employee $salary")
      sender ! updateSalary(salary).unsafeToFuture()
    case DeleteSalary(id) =>
      log.info(s"Got message to delete salary of id : $id")
      sender ! deleteSalary(id).unsafeToFuture()

    case GetAllBankDetails =>
      log.info(s"Got message to get the all bank details")
      getAllBankDetails.unsafeToFuture().pipeTo(sender)(self)
    case GetBankDetails(id) =>
      log.info(s"Got message to get the bank details of id : $id")
      sender ! getEmployeeBankDetails(id).unsafeToFuture()
    case UpdateBankDetails(bankDetails) =>
      log.info(s"Got message to update the bank details $bankDetails")
      sender ! updateBankDetails(bankDetails).unsafeToFuture()
    case DeleteBankDetails(id) =>
      log.info(s"Got message to delete bank details $id")
      sender ! deleteBankDetails(id).unsafeToFuture()
  }
}
