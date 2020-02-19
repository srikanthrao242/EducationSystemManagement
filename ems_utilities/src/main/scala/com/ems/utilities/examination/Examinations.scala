package com.ems.utilities.examination

import java.sql.Date
import spray.json._
import DefaultJsonProtocol._

case class Examinations_Details(ExamID: Option[Int], ExamName: String, ExamFor: String, TotalMarks: Int, ExamDate: Date, CreatedDate: Date)

case class Examinations_Subjects(SubjectID: Option[Int], ExamID: Int, Subject: String, ExamDate: Date, CreatedDate: Date, TotalMarks: Int)

case class Student_Marks(ID: Option[Int], StudentID: Int, ExamID: Int, SubjectID: Int, Marks: Int)

object ExaminationsSer{
  import com.ems.utilities.Utils._
  implicit val examinations_DetailsF: RootJsonFormat[Examinations_Details] = jsonFormat6(Examinations_Details)
  implicit val examinations_SubjectsF: RootJsonFormat[Examinations_Subjects] = jsonFormat6(Examinations_Subjects)
  implicit val student_MarksF: RootJsonFormat[Student_Marks] = jsonFormat5(Student_Marks)
}