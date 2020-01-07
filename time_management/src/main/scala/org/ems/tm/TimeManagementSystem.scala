/**/
package org.ems.tm

import akka.actor.Actor
import akka.event.slf4j.SLF4JLogging
import org.ems.tm.etities._
import org.ems.tm.services.{HolidayService, TimeSheetService}
import akka.pattern.pipe
import org.ems.tm.config.TimeConfig
class TimeManagementSystem
  extends Actor
  with TimeSheetService
  with HolidayService
  with SLF4JLogging {
  import context.dispatcher
  override def receive: Receive = {
    case GetTimeSheet(userId, id) =>
      log.info(s"Got message to get the time sheet data of id: $id")
      getTimeSheet(getDB(userId),id).unsafeToFuture().pipeTo(sender)(self)
    case AddTimeSheet(userId,timeSheet) =>
      log.info(s"Got message to add time sheet $timeSheet")
      sender ! addTimeSheet(getDB(userId),timeSheet).unsafeToFuture()
    case UpdateTimeSheet(userId,timeSheet) =>
      log.info(s"Got message to update the time sheet $timeSheet")
      sender ! updateTimeSheet(getDB(userId),timeSheet).unsafeToFuture()

    case GetHolidays(userId,id) =>
      log.info(s"Got message to get holiday details of id $id")
      sender ! getHolidays(getDB(userId),id).unsafeToFuture()
    case AddHoliday(userId,holidays) =>
      log.info(s"Got message to add holiday $holidays")
      sender ! addHoliday(getDB(userId),holidays).unsafeToFuture()
    case UpdateHolidays(userId,holidays) =>
      log.info(s"Got message to update the holiday $holidays")
      sender ! updateHolidays(getDB(userId),holidays).unsafeToFuture()
  }

  def getDB(userId: Int) : String = s"${TimeConfig.config.constants.db_prefix}_$userId"
}
