package controllers

import formats.APIJsonFormats

import models._
import play.api.Logger
import play.api.mvc._

import scala.concurrent.ExecutionContext.Implicits.global
import play.api.libs.json.Json

object Stories extends Controller with APIJsonFormats {
  def getAll(limit: Int, orderBy: String) = Action.async { request =>
      val futureStory = OldStory.getAll(limit,orderBy)
      futureStory.map{
        results =>
          Ok(Json.toJson(TopLevel(stories = Some(Right(Story.oldStoriesToStories(results))))))
      }
  }

  def getById(id: String) = Action.async { request =>
    OldStory.getById(id).map {
      case None =>
        NotFound(Error.toTopLevelJson(Error("No story account for this id %s and %s creationDate".format(id,OldStory.newIdToOldId(id)))))
      case Some(story) =>
        Ok(Json.toJson(TopLevel(stories = Some(Left(Story.oldStoryToStory(story))))))
    }
  }

  def getBySlug(slug: String) = Action.async { request =>
    OldStory.getBySlug(slug).map {
      result =>
       Ok(Json.toJson(TopLevel(stories = Some(Left(Story.oldStoryToStory(result))))))
    }
  }
}
