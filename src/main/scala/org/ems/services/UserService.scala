/**/
package org.ems.services

import akka.actor.ActorRef
import akka.util.Timeout
import org.ems.um.entities._

import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future}
import akka.pattern.ask

class UserService(user: ActorRef)(implicit ec: ExecutionContext) {
  implicit val timeout = Timeout(5 seconds)

  def getAllUsers: Future[List[User]] =
    for {
      users <- (user ? GetAllUsers).mapTo[List[User]]
    } yield users

  def getUser(id: Int): Future[Option[User]] =
    for {
      us <- (user ? GetUser(id)).mapTo[Option[User]]
    } yield us

  def deleteUser(id: Int): Future[Int] =
    for {
      du <- (user ? DeleteUser(id)).mapTo[Int]
    } yield du

  def addUser(userIn: User): Future[Int] =
    for {
      au <- (user ? AddUser(userIn)).mapTo[Int]
    } yield au

  def updateUser(userIn: User): Future[Int] = {
    if(userIn.id.isEmpty){
      throw new Exception("Id is mandatory Field for update")
    }else{
      for {
        uu <- (user ? UpdateUser(userIn)).mapTo[Int]
      } yield uu
    }
  }

}
