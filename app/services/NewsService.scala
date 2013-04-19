package services

import play.api.libs.concurrent.Execution.Implicits.defaultContext
import scala.concurrent._
import lib.Timer

class NewsService(waitTime: Int) {

  def getNewsNonBlocking(): Future[Unit] = future {
    Timer.delay(waitTime)
  }


  def getNewsBlocking() {
    Thread.sleep(waitTime)
  }
}
