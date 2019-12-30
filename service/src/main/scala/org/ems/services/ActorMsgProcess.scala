/**/
package org.ems.services

import akka.actor.ActorRef
import akka.pattern.ask
import akka.util.Timeout
import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future}

class ActorMsgProcess(ref: ActorRef)(implicit ec:ExecutionContext) {
  implicit val timeout = Timeout(100 seconds)
  def doProcess[T, R](msg: T): Future[R] =
    for {
      v <- (ref ? msg).mapTo[R]
    } yield v
}
