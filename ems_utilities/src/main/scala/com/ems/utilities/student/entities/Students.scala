package com.ems.utilities.student.entities

import java.sql.Timestamp
import spray.json._
import DefaultJsonProtocol._

case class StudentDetails(
                           StudentID: Option[Int],
                           FirstName: String,
                           LastName: String,
                           DOB: Timestamp,
                           BloodGroup: String,
                           Gender: String,
                           Religion: String,
                           Address: String,
                           City: String,
                           State: String,
                           PinCode: String,
                           Mobile: String,
                           Country: String,
                           Email: String,
                           IsActive: Boolean,
                           ProfileImage: String
                         )

case class ParentDetails(
                          ID: Option[Int],
                          FatherName: String,
                          MotherName: String,
                          FatherOccupation: String,
                          FatherQualification: String,
                          StudentID: Int,
                          Religion: String,
                          Address: String,
                          City: String,
                          State: String,
                          PinCode: String,
                          Country: String,
                          Mobile: String,
                          Email: String,
                          MotherOccupation:String,
                          MotherQualification:String
                        )

case class Admission(
                      AdmissionID: Option[Int],
                      StudentID: Int,
                      ClassID: Int,
                      SectionID: Int,
                      AdmissionDate: Int,
                      AcademicID: Int
                    )

case class AdmissionFeeDetails(
                                ID: Option[Int],
                                AdmissionID: Int,
                                StudentID: Int,
                                AdmisionFee: Double,
                                PaidFee: Double,
                                PaidDate: Timestamp
                              )

case class Classes(
                    ClassID: Option[Int],
                    ClassName: String,
                    AcademicID: Int,
                    NumberOfSections: Int,
                    Fee: Double,
                    FeeType: String
                  )

case class ClassSections(
                          SectionID: Option[Int],
                          SectionName: String,
                          TakeCarer: Int,
                          RoomDetails: String,
                          ClassID: Option[Int]
                        )

case class ClassSectionDataSource(
                                   ClassName: String,
                                   NumberOfSections: Int,
                                   Fee: Double,
                                   FeeType: String,
                                   sections: List[ClassSections]
                                 )

case class ClassCreateRequest(
                               classes: Classes,
                               sections: List[ClassSections]
                             )

object StudentSer {

  import com.ems.utilities.Utils._

  implicit val studentDetailsF: RootJsonFormat[StudentDetails] = jsonFormat16(StudentDetails)
  implicit val parentDetailsF: RootJsonFormat[ParentDetails] = jsonFormat16(ParentDetails)
  implicit val admissionF: RootJsonFormat[Admission] = jsonFormat6(Admission)
  implicit val admissionFeeDetailsF: RootJsonFormat[AdmissionFeeDetails] = jsonFormat6(AdmissionFeeDetails)
  implicit val classesF: RootJsonFormat[Classes] = jsonFormat6(Classes)
  implicit val classSectionsF: RootJsonFormat[ClassSections] = jsonFormat5(ClassSections)
  implicit val classCreateRequestF: RootJsonFormat[ClassCreateRequest] = jsonFormat2(ClassCreateRequest)
  implicit val classSectionDataSourceF: RootJsonFormat[ClassSectionDataSource] = jsonFormat5(ClassSectionDataSource)

}