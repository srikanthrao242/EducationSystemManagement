/**/
package com.ems.student.class_details

import akka.event.slf4j.SLF4JLogging
import com.ems.student.database.DbModule
import doobie.Update
import doobie._
import doobie.implicits._
import scala.concurrent.{ExecutionContext, Future}

trait ClassAndSectionSchema extends SLF4JLogging {

  implicit val executor: ExecutionContext
  def createSchemaForNewAcademic(db: String,
                                 academicId: Int,
                                 classId: Int,
                                 sectionId: Int): Future[Int] = {
    val schema = s"${db}_${academicId}_${classId}_$sectionId"
    for {
      _ <- createDBForAcClsSec(schema)
      stdID <- createStudent(schema, db)
      _ <- createStudentMarks(schema,db)
    } yield {
      stdID
    }
  }

  def createDBForAcClsSec(db: String): Future[Int] = {
    val query = s"CREATE DATABASE IF NOT EXISTS $db"
    log.debug(query)
    DbModule.transactor.use { xa =>
      Update(query).run().transact(xa)
    }.unsafeToFuture()
  }

  def createStudent(schema: String, db: String): Future[Int] = {
    val query =
      s"""
         |CREATE TABLE IF NOT EXISTS `$schema`.`students` (
         |  `ID` INT NOT NULL AUTO_INCREMENT,
         |  `StudentID` INT NULL,
         |  PRIMARY KEY (`ID`),
         |  INDEX `studentId_student_idx` (`StudentID` ASC) VISIBLE,
         |  CONSTRAINT `studentId_student`
         |    FOREIGN KEY (`StudentID`)
         |    REFERENCES `$db`.`student_details` (`StudentID`)
         |    ON DELETE NO ACTION
         |    ON UPDATE NO ACTION);
         |""".stripMargin
    log.debug(query)
    DbModule.transactor.use { xa =>
      Update(query).run().transact(xa)
    }.unsafeToFuture()
  }

  def createStudentMarks(schema: String, db: String): Future[Int] ={
    val query =
      s"""
        |CREATE TABLE `$schema`.`student_marks` (
        |  `ID` INT NOT NULL AUTO_INCREMENT,
        |  `StudentID` INT NULL,
        |  `ExamID` INT NULL,
        |  `SubjectID` INT NULL,
        |  `Marks` INT NULL,
        |  PRIMARY KEY (`ID`),
        |  INDEX `marks_exam_idx` (`ExamID` ASC) VISIBLE,
        |  INDEX `marks_student_idx` (`StudentID` ASC) VISIBLE,
        |  INDEX `marks_subject_idx` (`SubjectID` ASC) VISIBLE,
        |  CONSTRAINT `marks_exam`
        |    FOREIGN KEY (`ExamID`)
        |    REFERENCES `$db`.`examinations` (`ExamID`)
        |    ON DELETE NO ACTION
        |    ON UPDATE NO ACTION,
        |  CONSTRAINT `marks_student`
        |    FOREIGN KEY (`StudentID`)
        |    REFERENCES `$db`.`student_details` (`StudentID`)
        |    ON DELETE NO ACTION
        |    ON UPDATE NO ACTION,
        |  CONSTRAINT `marks_subject`
        |    FOREIGN KEY (`SubjectID`)
        |    REFERENCES `$db`.`examination_subjects` (`SubjectID`)
        |    ON DELETE NO ACTION
        |    ON UPDATE NO ACTION);
        |""".stripMargin
    log.debug(query)
    DbModule.transactor.use { xa =>
      Update(query).run().transact(xa)
    }.unsafeToFuture()
  }


}
