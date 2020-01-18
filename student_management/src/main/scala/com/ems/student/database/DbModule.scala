/**/
package com.ems.student.database

import cats.effect._
import com.ems.student.config.StudentConfiguration
import com.ems.utilities.student.config.DBConfig
import doobie.hikari.HikariTransactor
import doobie.util.ExecutionContexts

object DbModule {

  val db : DBConfig = StudentConfiguration.config.db
  implicit val cs: ContextShift[IO] =
    IO.contextShift(ExecutionContexts.synchronous)

  val transactor: Resource[IO, HikariTransactor[IO]] =
  for {
    ce <- ExecutionContexts.fixedThreadPool[IO](db.poolSize) // our connect EC
    be <- Blocker[IO] // our blocking EC
    xa <- HikariTransactor.newHikariTransactor[IO](
      db.driver,
      db.url,
      db.username,
      db.password,
      ce, // await connection here
      be // execute JDBC operations here
    )
  } yield xa

  def getDB(userId: Int) : String = s"${StudentConfiguration.config.constants.db_prefix}_$userId"
}
