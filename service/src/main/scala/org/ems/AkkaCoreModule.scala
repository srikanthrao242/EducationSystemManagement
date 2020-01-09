/**/
package org.ems

import akka.actor.ActorSystem
import akka.stream.Materializer

import scala.concurrent.ExecutionContextExecutor

trait AkkaCoreModule {
  implicit val actorSystem: ActorSystem = ActorSystem()
  implicit val executor: ExecutionContextExecutor = actorSystem.dispatcher
  implicit val materialize: Materializer = Materializer(actorSystem)
}
