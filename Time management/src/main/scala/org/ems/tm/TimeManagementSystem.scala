/**/
package org.ems.tm

import akka.actor.Actor
import akka.event.slf4j.SLF4JLogging
import org.ems.tm.etities._
import org.ems.tm.services.{HolidayService, TimeSheetService}
import akka.pattern.pipe
class TimeManagementSystem
  extends Actor
  with TimeSheetService
  with HolidayService
  with SLF4JLogging {
  import context.dispatcher
  override def receive: Receive = {
    case GetTimeSheet(id) =>
      log.info(s"Got message to get the time sheet data of id: $id")
      getTimeSheet(id).unsafeToFuture().pipeTo(sender)(self)
    case AddTimeSheet(timeSheet) =>
      log.info(s"Got message to add time sheet $timeSheet")
      sender ! addTimeSheet(timeSheet).unsafeToFuture()
    case UpdateTimeSheet(timeSheet) =>
      log.info(s"Got message to update the time sheet $timeSheet")
      sender ! updateTimeSheet(timeSheet).unsafeToFuture()

    case GetHolidays(id) =>
      log.info(s"Got message to get holiday details of id $id")
      sender ! getHolidays(id).unsafeToFuture()
    case AddHoliday(holidays) =>
      log.info(s"Got message to add holiday $holidays")
      sender ! addHoliday(holidays).unsafeToFuture()
    case UpdateHolidays(holidays) =>
      log.info(s"Got message to update the holiday $holidays")
      sender ! updateHolidays(holidays).unsafeToFuture()
  }
}
