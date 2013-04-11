package controllers

import play.api.mvc._
import concurrent.ExecutionContext.Implicits.global
import lib.AddressService

object Application extends Controller {

  val addressService = new AddressService

  def getTitle(address: String) = Action {

    println(s"received request for $address")

    Async {
      addressService.getTitleNonBlocking(address).map(Ok(_))
    }
  }
}
