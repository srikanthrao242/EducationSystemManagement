/**/
package org.ems.tm.database

import cats.effect._
import doobie.hikari.HikariTransactor
import doobie.util.ExecutionContexts
import org.ems.tm.config.{DBConfig, TimeConfig}

object DbModule {

  val db : DBConfig = TimeConfig.config.db
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

}
