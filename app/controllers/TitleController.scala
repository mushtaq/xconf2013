package controllers

import play.api.mvc._
import concurrent.ExecutionContext.Implicits.global
import lib.TitleService

object TitleController extends Controller {

  private val titleService = new TitleService

  def getTitle(address: String) = Action {

    Async {
      val titleFuture = titleService.getTitle(address)
      titleFuture.map(Ok(_))
    }
  }

  def getTitleBlocking(address: String) = Action {

    val title = titleService.getTitleBlocking(address)
    Ok(title)
  }
}
