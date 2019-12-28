/**/
package org.ems.um.database

import akka.event.slf4j.SLF4JLogging
import cats.effect._
import doobie._
import doobie.implicits._
import org.ems.um.config.UserConfig
import org.ems.um.database.ImportExportDao.log
import spray.json._
import org.ems.um.entities.{Authenticate, User}
import org.ems.um.entities.UserSer._
object ImportExportDao extends SLF4JLogging {
  val table = "user"
  val keyCol = "id"

  val columns = List(
    "address",
    "city",
    "name",
    "email",
    "mobile",
    "id",
    "registerdate",
    "registrationexp",
    "companyid",
    "usertype",
    "profileimg",
    "signature",
    "createdby",
    "isActive",
    "password"
  )

  def insertUser(user: User): IO[Int] = {
    log.debug(s"user To Insert $user")
    val queryStr =
      s"""INSERT INTO $table (${columns.mkString(", ")})
        VALUES (${List.fill(columns.length)("?").mkString(", ")})"""
    log.debug(s"$queryStr")
    DbModule.transactor.use { xa =>
      Update[User](queryStr)
        .withUniqueGeneratedKeys[Int](keyCol)(user)
        .transact(xa)
    }
  }

  def deleteUser(id: Int): IO[Int] = {
    val queryString = s"""DELETE FROM $table where $keyCol = ?"""
    log.debug(s"$queryString")
    DbModule.transactor.use { xa =>
      {
        Update[Int](queryString).run(id).transact(xa)
      }
    }
  }

  def updateUser(user: User): IO[Int] = {
    val id: Int = user.id.fold(
      throw new Exception(s"id field is compulsary for update")
    )(v => v)
    val userFields = user.toJson.asJsObject.fields.keys.toList
    val queryString =
      s"""UPDATE $table SET
         ${userFields.map(_ + " = ?").mkString(", ")}
         WHERE $keyCol = ?
         """
    log.debug(s"$queryString")
    DbModule.transactor.use { xa =>
      Update[(Int, User)](queryString)
        .run((id, user))
        .transact(xa)
    }
  }

  def findUser(id: Int): IO[Option[User]] = {
    val queryString =
      s"""SELECT ${columns.mkString(", ")}
          FROM $table WHERE $keyCol = ? """
    log.debug(s"$queryString")
    DbModule.transactor.use { xa =>
      {
        Query[Int, User](queryString).option(id).transact(xa)
      }
    }
  }

  def findAllUsers(): IO[List[User]] = {
    val queryString = s""" SELECT ${columns.mkString(", ")}
                FROM $table"""
    log.debug(s"$queryString")
    DbModule.transactor.use { xa =>
      {
        Query0[User](queryString).stream.transact(xa).compile.toList
      }
    }
  }

  def checkAuthentication(auth: Authenticate): IO[Option[User]] = {
    val queryString =
      s""" select ${columns.mkString(", ")}
         | from $table where email = '${auth.email}' and password = '${auth.password}'
         |""".stripMargin
    log.debug(s"$queryString")
    DbModule.transactor.use { xa =>
      {
        Query0[User](queryString).option.transact(xa)
      }
    }
  }



}
