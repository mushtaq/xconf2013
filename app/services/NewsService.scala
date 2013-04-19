package services

import play.api.libs.concurrent.Execution.Implicits.defaultContext
import scala.concurrent._
import lib.Timer

class NewsService(waitTime: Int) {

  val counter = Iterator.from(1)

  def getNewsNonBlocking(): Future[Unit] = future {
    val count = counter.next()
    println(s"before request number: $count")
    Timer.delay(waitTime)
    println(s"after request number: $count")
  }


  def getNewsBlocking() {
    val count = counter.next()
    println(s"before request number: $count")
    Thread.sleep(waitTime)
    println(s"after request number: $count")
  }
}
