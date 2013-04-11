package controllers

import play.api.mvc._
import concurrent.ExecutionContext.Implicits.global
import lib.AddressService
import play.api.Logger

object Application extends Controller {

  private val addressService = new AddressService

  def getTitle(method: String, address: String) = Action {

    Logger.debug(s"received request for $address")

    Async {
      val titleFuture = addressService.getTitle(method, address)
      titleFuture.map { title =>
        Logger.info(s"Title for $address is ${title.trim.lines.next()}")
        Ok(title)
      }
    }
  }

  def getTitleBlocking(address: String) = Action {

    Logger.debug(s"received request for $address")

    val title = addressService.getTitleBlocking(address)
    Logger.info(s"Title for $address is ${title.trim.lines.next()}")
    Ok(title)
  }
}
