/**/
package org.ems

import akka.actor.ActorSystem

import scala.concurrent.ExecutionContextExecutor

trait AkkaCoreModule {
  implicit val actorSystem: ActorSystem = ActorSystem()
  implicit val executor: ExecutionContextExecutor = actorSystem.dispatcher
}
