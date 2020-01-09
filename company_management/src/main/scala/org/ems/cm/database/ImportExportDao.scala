/**/
package org.ems.cm.database

import akka.event.slf4j.SLF4JLogging
import cats.effect._
import doobie._
import doobie.implicits._
import org.ems.cm.entities.Company
import spray.json._
import org.ems.cm.entities.CompanySer._
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
                     "registrationexp",
                     "companylogo",
                     "whatsup",
                     "isActive",
                     "numberofdays")

  def insertCompany(company: Company): IO[Int] = {
    log.debug(s"company To Insert $company")
    val queryStr =
      s"""INSERT INTO $table (${columns.mkString(", ")})
        VALUES (${List.fill(columns.length)("?").mkString(", ")})"""
    log.debug(s"$queryStr")
    DbModule.transactor.use { xa =>
      Update[Company](queryStr,None)
        .withUniqueGeneratedKeys[Int](keyCol)(company)
        .transact(xa)
    }
  }

  def deleteCompany(id: Int): IO[Int] = {
    val queryString = s"""DELETE FROM $table where $keyCol = ?"""
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

  def doActivation(id:Int, isActive:Boolean): IO[Int] ={
    val query = s"UPDATE `companies` SET `isActive` = $isActive WHERE (`id` = '$id')"
    log.debug(s"Query to update company activation $query")
    DbModule.transactor.use{xa=>
      Update(query).run().transact(xa)
    }
  }

  def getCompanyImage(id: Int): IO[Option[String]] = DbModule.transactor.use { xa =>
    Query[Int, String](s"""
                SELECT companylogo
                FROM companies
                WHERE id = ?
              """).option(id).transact(xa)
  }
}
