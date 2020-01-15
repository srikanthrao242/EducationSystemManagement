/**/
package com.ems.utilities.time_mgt.etities

import java.sql.Timestamp
import spray.json._
import DefaultJsonProtocol._

case class Holidays(id: Option[Int], holidayType:String , date: Timestamp)

case class TimeSheet(
  id: Option[Int],
  employeeId: Int,
  date: Timestamp,
  timeFrom: Timestamp,
  timeTo: Timestamp,
  numberOfHours: String,
  comments: String,
  dateSubmitted: Timestamp
)

object TimeSheetSer{
  implicit object DateJsonFormat extends RootJsonFormat[Timestamp] {
    override def write(obj: Timestamp): JsString = JsString(obj.toString)
    override def read(json: JsValue): Timestamp = json match {
      case JsString(s) => Timestamp.valueOf(s)
      case _ =>
        throw new DeserializationException("Error info you want here ...")
    }
  }
  implicit val timeSheetF: RootJsonFormat[TimeSheet] = jsonFormat8(TimeSheet)
  implicit val holidaysF: RootJsonFormat[Holidays] = jsonFormat3(Holidays)
}
