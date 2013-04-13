package lib

import play.api.libs.ws.WS
import scala.util.{Failure, Success}
import concurrent._
import concurrent.ExecutionContext.Implicits.global

object Linking {

  def eventBasedComposition = {
    var total = 0
    WS.url("http://www.google.com/stock/price/infy").get().onComplete {
      case Success(response1) => WS.url("http://www.google.com/stock/price/wipro").get().onComplete {
        case Success(response2) => total = response1.body.toInt + response2.body.toInt
        case Failure(ex)        => ex.printStackTrace()
      }
      case Failure(ex)        => ex.printStackTrace()
    }
    total
  }

  def futureBasedComposition = for {
    response1 <- WS.url("http://www.google.com/stock/price/infy").get()
    response2 <- WS.url("http://www.google.com/stock/price/wipro").get()
  } yield response1.body.toInt + response2.body.toInt

  def event2Future(url: String) = {
    val p = promise[String]()
    WS.url(url).get().onComplete {
      case Success(response) => p.complete(Success("Successful!!!"))
      case Failure(ex)       => p.complete(Failure(ex))
    }
    p.future
  }
}
