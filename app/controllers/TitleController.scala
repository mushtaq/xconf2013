package controllers

import play.api.mvc._
import concurrent.ExecutionContext.Implicits.global
import lib.TitleService
import play.api.Logger

object TitleController extends Controller {

  private val titleService = new TitleService

  def getTitle(address: String) = Action {

    Logger.debug(s"received request for $address")

    Async {
      val titleFuture = titleService.getTitle(address)
      titleFuture.map { title =>
        Logger.info(s"Title for $address is ${title.trim.lines.next()}")
        Ok(title)
      }
    }
  }

  def getTitleBlocking(address: String) = Action {

    Logger.debug(s"received request for $address")

    val title = titleService.getTitleBlocking(address)
    Logger.info(s"Title for $address is ${title.trim.lines.next()}")
    Ok(title)
  }
}
