/**/
package org.ems.um.services

import org.ems.um.database.{ImportExportDao, UserSchema}
import org.ems.um.entities.{Activation, AddUser, Authenticate, DeleteUser, GetUser, UpdateUser, User}

import scala.concurrent.{ExecutionContext, Future}

trait UserService extends UserSchema{

  implicit val executor : ExecutionContext

  def getAllUsers: Future[List[User]] =
    for {
      users <-ImportExportDao.findAllUsers().unsafeToFuture()
    } yield users

  def getUser(id: Int): Future[Option[User]] =
    for {
      us <- ImportExportDao.findUser(id).unsafeToFuture()
    } yield us

  def deleteUser(id: Int): Future[Int] =
    for {
      du <- ImportExportDao.deleteUser(id).unsafeToFuture()
    } yield du

  def addUser(userIn: User): Future[Int] =
    for {
      au <- ImportExportDao.insertUser(userIn).unsafeToFuture()
      _ <- createSchema(au)
    } yield au

  def updateUser(userIn: User): Future[Int] =
    if (userIn.id.isEmpty) {
      throw new Exception("Id is mandatory Field for update")
    } else {
      for {
        uu <- ImportExportDao.updateUser(userIn).unsafeToFuture()
      } yield uu
    }

  def authenticateUser(auth: Authenticate): Future[Option[User]] =
    for {
      uu <- ImportExportDao.checkAuthentication(auth).unsafeToFuture()
    } yield uu

  def activation(id:Int, act:Activation): Future[Int] = {
    for{
      d <- ImportExportDao.doActivation(id, act.isActivate).unsafeToFuture()
    }yield d
  }
}
