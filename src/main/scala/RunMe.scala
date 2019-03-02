import java.util.concurrent.TimeUnit

import akka.actor.ActorRef

import scala.concurrent.Await
import scala.concurrent.duration.Duration


object RunMe extends ActorConfig with App {

  try {
    val master: ActorRef = system.actorOf(Master.props, name = "master")
    master ! FindMeAWorthyLeader
  } finally {
    Await.ready(system.whenTerminated, Duration(5, TimeUnit.SECONDS))
  }

}
