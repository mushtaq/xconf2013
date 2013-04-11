package controllers

import play.api.mvc._
import concurrent.ExecutionContext.Implicits.global
import lib.AddressService
import play.api.Logger

object Application extends Controller {

  val addressService = new AddressService

  def getTitle(address: String) = Action {

    Logger.debug(s"received request for $address")

    Async {
      addressService.getTitleNonBlocking(address).map { title =>
        Logger.info(s"Title for $address is ${title.trim.lines.next()}")
        Ok(title)
      }
    }
  }
}
