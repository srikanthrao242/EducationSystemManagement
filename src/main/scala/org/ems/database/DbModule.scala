/**/
package org.ems.database

import cats.effect.{ContextShift, IO}
import doobie._
import doobie.util.ExecutionContexts
import doobie.util.transactor.Transactor.Aux
import org.ems.config.{EMS, ESMConfig}
trait DbModule {
  val etasConfig: EMS = ESMConfig.config
  implicit val cs: ContextShift[IO] = IO.contextShift(ExecutionContexts.synchronous)
  val db_transactor: Aux[IO, Unit] = Transactor.fromDriverManager[IO](
    etasConfig.db.driver,
    etasConfig.db.url,
    etasConfig.db.username, etasConfig.db.password
  )
}
