/**/
package org.ems.database

import akka.event.slf4j.SLF4JLogging
import cats.effect._
import doobie._
import doobie.implicits._
import org.ems.entities.{Company, InsertCompany}
import spray.json._
import org.ems.entities.CompanySer._

object ImportExportDao extends SLF4JLogging {
  val table = "companies"
  val keyCol = "id"
  val columns = List("address",
                     "city",
                     "companyname",
                     "email",
                     "mobile",
                     "id",
                     "registerdate",
                     "registrationexp")

  def insertCompany(company: InsertCompany): IO[Int] = {
    log.debug(s"company To Insert $company")
    val companyFields = company.toJson.asJsObject.fields.keys.toList
    val queryStr =
      s"""INSERT INTO $table (${companyFields.mkString(", ")})
        VALUES (${List.fill(companyFields.length)("?").mkString(", ")})"""
    log.debug(s"$queryStr")
    DbModule.transactor.use { xa =>
      Update[InsertCompany](queryStr)
        .withUniqueGeneratedKeys[Int](keyCol)(company)
        .transact(xa)
    }
  }

  def deleteCompany(id: Int): IO[Int] = {
    val queryString = s"""DELETE FROM $table where $keyCol = $id"""
    log.debug(s"$queryString")
    DbModule.transactor.use { xa =>
      {
        Update[Int](queryString).run(id).transact(xa)
      }
    }
  }

  def updateCompany(company: Company): IO[Int] = {
    val id: Int = company.id.fold(
      throw new Exception(s"id field is compulsary for update")
    )(v => v)
    val companyFields = company.toJson.asJsObject.fields.keys.toList
    val queryString =
      s"""UPDATE $table SET
         ${companyFields.map(_ + " = ?").mkString(", ")}
         WHERE $keyCol = ?
         """
    log.debug(s"$queryString")
    DbModule.transactor.use { xa =>
      Update[(Int, Company)](queryString)
        .run((id, company))
        .transact(xa)
    }
  }

  def findCompany(id: Int): IO[Option[Company]] = {
    val queryString =
      s"""SELECT ${columns.mkString(", ")}
          FROM $table WHERE $keyCol = ? """
    log.debug(s"$queryString")
    DbModule.transactor.use { xa =>
      {
        Query[Int, Company](queryString).option(id).transact(xa)
      }
    }
  }

  def findAllCompanies(): IO[List[Company]] = {
    val queryString = s""" SELECT ${columns.mkString(", ")}
                FROM $table"""
    log.debug(s"$queryString")
    DbModule.transactor.use { xa =>
      {
        Query0[Company](queryString).stream.transact(xa).compile.toList
      }
    }
  }
}
