package lib

import play.api.libs.ws.WS
import concurrent.ExecutionContext.Implicits.global
import scala.util.Try
import scala.concurrent._
import play.api.Logger

class AddressService {

  private val TitleRegex = """<title>([^<]+)</title>""".r

  def getTitle(method: String, address: String) = method match {
    case "nonblocking" => getTitleNonBlocking(address)
    case "async"       => getTitleAsync(address)
  }

  def getTitleBlocking(address: String) = Try {
    val responseBody = io.Source.fromURL(s"http://$address").mkString
    extract(responseBody)
  }.getOrElse("ERROR")


  def getTitleAsync(address: String) = future {
    val responseBody = io.Source.fromURL(s"http://$address").mkString
    extract(responseBody)
  }.recover(error(address))

  def getTitleNonBlocking(address: String) =
    WS.url(s"http://$address").get().map { response =>
      extract(response.body)
    }.recover(error(address))

  private def extract(body: String) =
    TitleRegex.findFirstMatchIn(body).map(_.group(1)).getOrElse("NO TITLE")

  private def error(address: String): PartialFunction[Throwable, String] = {
    case ex: Throwable =>
      Logger.error(s"Error in getting title for: $address. Error msg is: ${ex.getMessage}")
      "ERROR"
  }
}
