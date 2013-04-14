package lib

import play.api.libs.ws.WS
import concurrent.ExecutionContext.Implicits.global
import scala.util.Try
import play.api.Logger
import wrappers.TapperImplicits.Tapper
import scala.concurrent.Future

class TitleService {

  private val TitleRegex = """<title>([^<]+)</title>""".r

  def getTitleFuture(address: String): Future[String] = {
    address.tap("request: Getting title for")
    WS.url(s"http://$address").get().map { response =>
      extract(response.body).tap(s"RESPONSE: Title for $address is")
    }.recover(error(address))
  }

  def getTitle(address: String): String = Try {
    address.tap("request: Getting title for")
    val responseBody = io.Source.fromURL(s"http://$address").getLines().mkString
    extract(responseBody).tap(s"RESPONSE: Title for $address is")
  }.getOrElse("ERROR")

  private def extract(body: String) = {
    TitleRegex.findFirstMatchIn(body).map(_.group(1)).getOrElse("NO TITLE").trim.lines.next()
  }

  private def error(address: String): PartialFunction[Throwable, String] = {
    case ex: Throwable =>
      Logger.error(s"Error in getting title for: $address. Error msg is: ${ex.getMessage}")
      "ERROR"
  }
}
