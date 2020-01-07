/**/
package org.ems.um

import akka.actor.{Actor, Props}
import akka.event.slf4j.SLF4JLogging
import org.ems.um.database.{ImportExportDao, UserSchema}
import org.ems.um.entities._
import akka.pattern.pipe
import akka.pattern.ask
import akka.util.Timeout

import scala.concurrent.Future
import scala.concurrent.duration._

class UserManagementSystem extends Actor with SLF4JLogging {
  implicit val ec = context.system.dispatcher
  val userSchema = context.actorOf(Props[UserSchema], "UserSchema")
  implicit val timeout = Timeout(10 seconds)
  override def receive: Receive = {
    case AddUser(user: User) =>
      log.debug(s"got user to add $user")
      val res: Future[Int] = for {
        id <- ImportExportDao.insertUser(user).unsafeToFuture()
        _ = userSchema ! CreateSchema(id)
      } yield {
         id
      }
      res.pipeTo(sender)(self)
    case UpdateUser(user: User) =>
      log.debug(s"got user to update $user")
      ImportExportDao.updateUser(user).unsafeToFuture().pipeTo(sender)(self)
    case GetAllUsers =>
      log.debug(s"got message to get all users")
      ImportExportDao.findAllUsers().unsafeToFuture().pipeTo(sender)(self)
    case GetUser(id) =>
      log.debug(s"got message to get user of $id")
      ImportExportDao.findUser(id).unsafeToFuture().pipeTo(sender)(self)
    case DeleteUser(id) =>
      log.debug(s"got message to get user of $id")
      ImportExportDao.deleteUser(id).unsafeToFuture().pipeTo(sender)(self)
    case auth: Authenticate =>
      log.debug(s"got message to authenticate user of $auth")
      ImportExportDao
        .checkAuthentication(auth)
        .unsafeToFuture()
        .pipeTo(sender)(self)
    case _ =>
      log.debug(s"Got default message")
  }
}
