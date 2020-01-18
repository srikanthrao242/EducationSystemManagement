package com.ems.utilities.student.entities

import java.sql.Timestamp
import spray.json._
import DefaultJsonProtocol._

case class Academic(AcademicID:Option[Int],
                    AcademicName:String,
                    StartDate:Timestamp,
                    EndYear:Int,
                    EndDate:Option[Timestamp],
                    UserID:Int,
                    IsActive:Boolean,
                    IsCurrentAcademic:Boolean)


object AcademicSer{
  import com.ems.utilities.Utils._
  implicit val academicF: RootJsonFormat[Academic] = jsonFormat8(Academic)
}