/**/
package org.ems.tm.etities


case class GetTimeSheet(id:Int)
case class UpdateTimeSheet(timeSheet: TimeSheet)
case class AddTimeSheet(timeSheet: TimeSheet)

case class GetHolidays(id:Int)
case class UpdateHolidays(holidays: Holidays)
case class AddHoliday(holidays: Holidays)
case object UpdateWeekOffs

object RequestEntities{

}