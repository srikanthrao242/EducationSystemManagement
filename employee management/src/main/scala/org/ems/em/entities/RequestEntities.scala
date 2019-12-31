/**/
package org.ems.em.entities

case class GetAllEmployeesProfile(userId: Int)
case class GetEmployeeProfile(userId: Int, id: Int)
case class AddEmployeeProfile(userId: Int, employee: Employee)
case class UpdateEmployeeProfile(userId: Int, employee: Employee)
case class DeleteEmployeeProfile(userId: Int, id: Int)

case class GetAllSalaries(userId: Int)
case class GetEmployeeSalary(userId: Int, salaryId: Int)
case class UpdateSalary(userId: Int, salary: Salary)
case class DeleteSalary(userId: Int, id: Int)
case class AddEmployeeSalary(userId: Int, salary: Salary)

case class GetAllBankDetails(userId: Int)
case class GetBankDetails(userId: Int, id: Int)
case class UpdateBankDetails(userId: Int, bankDetails: BankDetails)
case class DeleteBankDetails(userId: Int, id: Int)
case class AddEmployeeBankDetails(userId: Int, bankDetails: BankDetails)

object RequestEntities {}
