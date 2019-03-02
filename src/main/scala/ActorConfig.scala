import akka.actor.ActorSystem

import scala.concurrent.ExecutionContext

trait ActorConfig {
  implicit val system: ActorSystem = ActorSystem("async-flood-max")
  implicit val executor: ExecutionContext = system.dispatcher
}
