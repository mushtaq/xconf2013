package xconf

import play.api.libs.ws.{Response, WS}
import scala.util.{Failure, Success}
import concurrent._
import concurrent.ExecutionContext.Implicits.global

object FuturesVsCallback extends App {

  val future1 = WS.url("http://www.google.com").get()
  val future2 = WS.url("http://www.mit.edu").get()

  def lineCount(response: Response) = response.body.lines.size

  def eventBasedComposition(success: Any => Any, failure: Throwable => Any) = {
    future1.onComplete {
      case Success(response1) => future2.onComplete {
        case Success(response2) =>
          success(lineCount(response1) + lineCount(response2))
        case Failure(ex)        =>
          failure(ex)
      }
      case Failure(ex)        =>
        failure(ex)
    }
  }

  def action1 = eventBasedComposition(
    success => println(success),
    failure => println(failure.getMessage)
  )

  action1

  def futureBasedComposition = for {
    response1 <- future1
    response2 <- future2
  } yield lineCount(response1) + lineCount(response2)

  def action2(res: Future[Int]) = res.map(_ + 10)
  def action3(res: Future[String]) = res.map("Hello " + _).recover {case _ => "Never mind"}
  def action4(res1: Future[Int], res2: Future[String]) = res1.zip(res2)

  action4(
    action2(futureBasedComposition),
    action3(future(11.asInstanceOf[String]))
  ).onComplete {
    case Success(tuple) => println(tuple)
    case Failure(ex) => println(ex.getMessage)
  }

}
