package com.ems.utilities.student.entities

import java.sql.Timestamp

case class Student(StdId: Option[Int],
                   FirstName: String,
                   MiddleName: String,
                   LastName: String,
                   DOB: Timestamp,
                   BloodGroup: Option[String],
                   Gender: String,
                   Religion: String,
                   Address: String,
                   City: String,
                   State: String,
                   PinCode: String,
                   Mobile: Option[String],
                   Country: String,
                   Email: Option[String],
                   IsActive: Boolean,
                   ProfileImage: Option[String])

case class ParentDetails(ParId: Option[Int],
                         FirstName: String,
                         LastName: String,
                         Occupations: String,
                         Qualification: String,
                         StdId: Int,
                         Religion: String,
                         Address: String,
                         City: String,
                         State: String,
                         PinCode: String,
                         Country: String,
                         Mobile: String,
                         Email: String)

case class Class(ClassId: Option[Int], ClassStandard: String, NumberOfSections: Int)

case class ClassSections(SectionId: Option[Int], ClassId: Int, ClassTeacher: Int, RoomDetails: Option[String])

case class Admission(AdmissionId: Option[Int],
                     StdId: Int,
                     ClassId: Int,
                     SectionId: Int,
                     AdmissionDate: Timestamp,
                     MonthlyFee: Double,
                     QuarterlyFee: Double,
                     HalfYearlyFee: Double,
                     YearlyFee: Double)

case class PreviousEducationDetails(EdId: Option[Int], StdId: Int, InstitutionName: String, Course: String, Year: String, TotalMarksObtained: Int)

case class AdmissionFeeDetails(id: Option[Int],
                               AdmissionId: Int,
                               StdId: Int,
                               AdmissionFee: Double,
                               ActualFee: Double,
                               PaidFee: Double,
                               PaidDate: Timestamp)

case class StudentFeeDetails(id:Option[Int],
                             Month:String,
                             PaidFee:Double,
                             DueFee:Double,
                             DueFeeForQuarterly:Double,
                             DueFeeForHalfYearly:Double,
                             DueFeeForYearly:Double,
                             AdmissionDueFee:Double,
                             StdId:Int,
                             AdmissionId:Int)