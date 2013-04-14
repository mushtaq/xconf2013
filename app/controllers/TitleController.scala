package controllers

import play.api.mvc._
import concurrent.ExecutionContext.Implicits.global
import lib.TitleService

object TitleController extends Controller {

  private val titleService = new TitleService

  def getTitleNonBlocking(address: String) = Action {

    Async {
      val titleFuture = titleService.getTitleFuture(address)
      titleFuture.map { title =>
        Ok(title)
      }
    }
  }

  def getTitle(address: String) = Action {

    val title = titleService.getTitle(address)
    Ok(title)
  }
}
