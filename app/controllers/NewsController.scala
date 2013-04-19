package controllers

import play.api.mvc._
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import services.NewsService
import scala.concurrent._

object NewsController extends Controller {

  val iter = Iterator.from(0).map(i => (i % 8) + 1)

  def processingTime = iter.next() * 100
  def process() = Thread.sleep(processingTime)

  val newsService = new NewsService(3000)

  def getNewsNonBlocking(count: Int) = Action {
    process()
    Async {
      Future.traverse((1 to count).toList) { i =>
        newsService.getNewsNonBlocking()
      }.map { res =>
        process()
        Ok("done")
      }
    }
  }

  def getNewsSequential(count: Int) = Action {
    process()
    (1 to count).map { i =>
      newsService.getNewsBlocking()
    }
    process()
    Ok("done")
  }

  def getNewsParBlocking(count: Int) = Action {
    process()
    Async {
      Future.traverse((1 to count).toList) { i =>
        future(newsService.getNewsBlocking())
      }.map { res =>
        process()
        Ok("done")
      }
    }
  }
}
