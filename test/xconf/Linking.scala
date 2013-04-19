package xconf

import play.api.libs.ws.{Response, WS}
import scala.util.{Failure, Success}
import concurrent._
import concurrent.ExecutionContext.Implicits.global

object Linking extends App {

  def event2Future(url: String) = {
    val p = promise[Response]()
    WS.url(url).get().onComplete {
      case Success(response) => p.complete(Success(response))
      case Failure(ex)       => p.complete(Failure(ex))
    }
    p.future
  }
}
