/**/
package com.ems.utilities.time_mgt.etities

case class GetTimeSheet(userId:Int,id:Int)
case class UpdateTimeSheet(userId:Int,timeSheet: TimeSheet)
case class AddTimeSheet(userId:Int,timeSheet: TimeSheet)

case class GetHolidays(userId:Int,id:Int)
case class UpdateHolidays(userId:Int,holidays: Holidays)
case class AddHoliday(userId:Int,holidays: Holidays)
case class UpdateWeekOffs(userId:Int)

object RequestEntities{

}