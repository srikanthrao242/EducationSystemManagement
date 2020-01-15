/**/
package org.ems.cm.database

import cats.effect._
import doobie.hikari.HikariTransactor
import doobie.util.ExecutionContexts
import com.ems.utilities.companies.config.{CompanyConfig, DBConfig}
import org.ems.cm.config.CompanyConfiguration

object DbModule {

  val db : DBConfig = CompanyConfiguration.config.db
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
