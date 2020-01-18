/**/
package org.ems.um.database

import akka.event.slf4j.SLF4JLogging
import cats.effect.IO
import doobie._
import doobie.implicits._
import doobie.util.update
import org.ems.um.config.UserConfiguration

import scala.concurrent.{ExecutionContext, Future}

trait UserSchema extends SLF4JLogging {
  implicit val executor: ExecutionContext

  def createSchema(userId: Int): Future[Int] = {
    log.debug(s"Got message to createschema $userId")
    val schema = s"${UserConfiguration.config.constants.db_prefix}_$userId"
    for {
      _ <- createDb(userId).unsafeToFuture()
      eid <- createEmployeeTable(schema).unsafeToFuture()
      _ <- createSalary(schema).unsafeToFuture()
      _ <- createBankDetails(schema).unsafeToFuture()
      _ <- createCreditDetails(schema).unsafeToFuture()
      _ <- createTimeSheet(schema).unsafeToFuture()
      _ <- createHolidays(schema).unsafeToFuture()
    } yield {
      log.debug(s"successfully created employee table")
      eid
    }
  }

  def createDb(userId: Int): IO[Int] = {
    val query =
      s"""CREATE DATABASE IF NOT EXISTS
         ${UserConfiguration.config.constants.db_prefix}_$userId
         """
    log.debug(query.toString())
    DbModule.transactor.use { xa =>
      Update(query).run().transact(xa)
    }
  }

  def createEmployeeTable(schema: String): IO[Int] = {
    val query =
      s"""
         |CREATE TABLE IF NOT EXISTS  `$schema`.`employees` (
         |  `id` INT NOT NULL AUTO_INCREMENT,
         |  `firstName` VARCHAR(45) NULL,
         |  `middleName` VARCHAR(45) NULL,
         |  `lastName` VARCHAR(45) NULL,
         |  `gender` VARCHAR(45) NULL,
         |  `dateOfJoining` DATETIME NULL,
         |  `dateOfRelieving` DATETIME NULL,
         |  `email` VARCHAR(45) NULL,
         |  `mobile` VARCHAR(13) NULL,
         |  `city` VARCHAR(45) NULL,
         |  `address` VARCHAR(100) NULL,
         |  `companyId` INT NULL,
         |  `designation` VARCHAR(45) NULL,
         |  `employeeType` VARCHAR(45) NULL,
         |  `qualification` VARCHAR(45) NULL,
         |  `isActive` TINYINT NULL DEFAULT '1',
         |  `employeeProfile` VARCHAR(100) DEFAULT 'user.png' ,
         |  PRIMARY KEY (`id`),
         |  INDEX `emp_comp_key_idx` (`companyId` ASC) VISIBLE,
         |  CONSTRAINT `emp_comp_key`
         |    FOREIGN KEY (`companyId`)
         |    REFERENCES `educationmanagementsystem`.`companies` (`id`)
         |    ON DELETE NO ACTION
         |    ON UPDATE NO ACTION)
         |""".stripMargin
    log.debug(query.toString())
    DbModule.transactor.use { xa =>
      Update(query).run().transact(xa)
    }
  }

  def createSalary(schema: String): IO[Int] = {
    val query =
      s"""
         |CREATE TABLE IF NOT EXISTS  `$schema`.`salary` (
         |  `id` INT NOT NULL AUTO_INCREMENT,
         |  `employeeId` INT NULL,
         |  `salaryPerHour` DOUBLE NULL DEFAULT 0,
         |  `salaryPerMon` DOUBLE NULL DEFAULT 0,
         |  `allowance` DOUBLE NULL,
         |  `allowanceDesc` VARCHAR(100) NULL,
         |  `deduction` DOUBLE NULL DEFAULT 0,
         |  `deductionDesc` VARCHAR(100) NULL,
         |  `taxPercentage` DOUBLE NULL,
         |  `salAfterTax` DOUBLE NULL DEFAULT 0,
         |  `salBeforeTax` DOUBLE NULL DEFAULT 0,
         |  `tax` DOUBLE NULL DEFAULT 0,
         |  `comments` VARCHAR(100) NULL,
         |  PRIMARY KEY (`id`),
         |  INDEX `emp_sal_key_idx` (`employeeId` ASC) VISIBLE,
         |  CONSTRAINT `emp_sal_key`
         |    FOREIGN KEY (`employeeId`)
         |    REFERENCES `$schema`.`employees` (`id`)
         |    ON DELETE NO ACTION
         |    ON UPDATE NO ACTION);
         |
         |""".stripMargin

    log.debug(query.toString())
    DbModule.transactor.use { xa =>
      Update(query).run().transact(xa)
    }
  }

  def createBankDetails(schema: String): IO[Int] = {
    val query =
      s"""
         |CREATE TABLE IF NOT EXISTS `$schema`.`bank_details` (
         |  `id` INT NOT NULL AUTO_INCREMENT,
         |  `employeeId` INT NULL,
         |  `bankName` VARCHAR(45) NULL,
         |  `branchCode` VARCHAR(45) NULL,
         |  `accNo` VARCHAR(20) NULL,
         |  PRIMARY KEY (`id`),
         |  INDEX `emp_bank_key_idx` (`employeeId` ASC) VISIBLE,
         |  CONSTRAINT `emp_bank_key`
         |    FOREIGN KEY (`employeeId`)
         |    REFERENCES `$schema`.`employees` (`id`)
         |    ON DELETE NO ACTION
         |    ON UPDATE NO ACTION);
         |
         |""".stripMargin
    log.debug(query.toString())
    DbModule.transactor.use { xa =>
      Update(query).run().transact(xa)
    }
  }

  def createCreditDetails(schema: String): IO[Int] = {
    val query =
      s"""
         |CREATE TABLE IF NOT EXISTS  `$schema`.`credit_salary` (
         |  `id` INT NOT NULL AUTO_INCREMENT,
         |  `employeeId` INT NULL,
         |  `creditedSalary` DOUBLE NULL DEFAULT 0,
         |  `presentDays` DOUBLE NULL,
         |  `workingDays` DOUBLE NULL DEFAULT 0,
         |  `month` VARCHAR(4) NULL,
         |  `year` INT NULL,
         |  `dateOfCredit` DATETIME NULL,
         |  PRIMARY KEY (`id`),
         |  INDEX `emp_credit_key_idx` (`employeeId` ASC) VISIBLE,
         |  CONSTRAINT `emp_credit_key`
         |    FOREIGN KEY (`employeeId`)
         |    REFERENCES `$schema`.`employees` (`id`)
         |    ON DELETE NO ACTION
         |    ON UPDATE NO ACTION);
         |
         |""".stripMargin
    log.debug(query.toString())
    DbModule.transactor.use { xa =>
      Update(query).run().transact(xa)
    }
  }

  def createTimeSheet(schema: String): IO[Int] = {
    val query =
      s"""
         |CREATE TABLE IF NOT EXISTS `$schema`.`time_sheet` (
         |  `id` INT NOT NULL AUTO_INCREMENT,
         |  `employeeId` INT NULL,
         |  `date` DATETIME NULL,
         |  `timeFrom` DATETIME NULL,
         |  `timeTo` DATETIME NULL,
         |  `numberOfHours` VARCHAR(10) NULL,
         |  `comments` VARCHAR(100) NULL,
         |  `dateSubmitted` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
         |  PRIMARY KEY (`id`),
         |  CONSTRAINT `emp_time_keys`
         |    FOREIGN KEY (`id`)
         |    REFERENCES `$schema`.`employees` (`id`)
         |    ON DELETE NO ACTION
         |    ON UPDATE NO ACTION);
         |""".stripMargin
    log.debug(query.toString())
    DbModule.transactor.use { xa =>
      Update(query).run().transact(xa)
    }
  }

  def createHolidays(schema: String): IO[Int] = {
    val query =
      s"""
         |CREATE TABLE IF NOT EXISTS `$schema`.`holidays` (
         |  `id` INT NOT NULL AUTO_INCREMENT,
         |  `holidayType` VARCHAR(45) NULL,
         |  `date` DATETIME NULL,
         |  PRIMARY KEY (`id`));
         |""".stripMargin

    log.debug(query.toString())
    DbModule.transactor.use { xa =>
      Update(query).run().transact(xa)
    }
  }

  def createAcademic(schema: String): IO[Int] = {
    val query =
      s"""
         |  CREATE TABLE `$schema`.`academic` (
         |  `AcademicID` INT NOT NULL AUTO_INCREMENT,
         |  `AcademicName` VARCHAR(45) NULL,
         |  `StartDate` TIMESTAMP NULL,
         |  `EndYear` YEAR NULL,
         |  `EndDate` TIMESTAMP NULL,
         |  `UserID` INT NULL,
         |  `IsActive` TINYINT NULL,
         |  `IsCurrentAcademic` TINYINT NULL,
         |  PRIMARY KEY (`AcademicID`),
         |  UNIQUE INDEX `AcademicName_UNIQUE` (`AcademicName` ASC) VISIBLE,
         |  INDEX `academic_user_idx` (`UserID` ASC) VISIBLE,
         |  CONSTRAINT `academic_user`
         |    FOREIGN KEY (`UserID`)
         |    REFERENCES `educationmanagementsystem`.`user` (`id`)
         |    ON DELETE NO ACTION
         |    ON UPDATE NO ACTION);
         |""".stripMargin
    log.debug(query.toString)
    DbModule.transactor.use { xa =>
      Update(query).run().transact(xa)
    }
  }

}
