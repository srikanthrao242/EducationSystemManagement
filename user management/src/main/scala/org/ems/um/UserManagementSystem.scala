/**/
package org.ems.um

import akka.actor.Actor
import akka.event.slf4j.SLF4JLogging
import org.ems.um.database.ImportExportDao
import org.ems.um.entities._
import akka.pattern.pipe

class UserManagementSystem extends Actor with SLF4JLogging {
  implicit val ec = context.system.dispatcher

  override def receive: Receive = {
    case AddUser(user: User) =>
      log.debug(s"got user to add $user")
      ImportExportDao.insertUser(user).unsafeToFuture().pipeTo(sender)(self)
    case UpdateUser(user: User) =>
      log.debug(s"got user to update $user")
      sender() ! ImportExportDao.updateUser(user).unsafeToFuture()
    case GetAllUsers =>
      log.debug(s"got message to get all users")
      ImportExportDao.findAllUsers().unsafeToFuture().pipeTo(sender)(self)
    case GetUser(id) =>
      log.debug(s"got message to get user of $id")
      ImportExportDao.findUser(id).unsafeToFuture().pipeTo(sender)(self)
    case DeleteUser(id) =>
      log.debug(s"got message to get user of $id")
      ImportExportDao.deleteUser(id).unsafeToFuture().pipeTo(sender)(self)
    case _ =>
      log.debug(s"Got default message")
  }
}
