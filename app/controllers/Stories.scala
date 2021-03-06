package controllers

import javax.inject.{Inject, Singleton}

import actions.{TokenRequest, LoggingAction, TokenCheckAction}
import dao.{TokenDao, ViewerProfileDao, StoryDao}
import formats.APIJsonFormats

import models._
import dto._
import play.Logger
import play.api.mvc._

import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json.{JsValue, Json}
import services.StoryService

import scala.concurrent.Future

@Singleton
class Stories @Inject()(tokenDao: TokenDao, storyService: StoryService, storyDao: StoryDao, tokenCheckAction: TokenCheckAction, viewerProfileDao: ViewerProfileDao) extends Controller with APIJsonFormats {

  def dispatcher(
                  limit: Int, orderBy: String, filterBy: String,
                  slug: Option[String], sinceId: Option[String], lastSkippedId: Option[String]) = LoggingAction {
    tokenCheckAction.async { request =>
      Logger.info(s"start matching $slug")
      slug match {
        case None =>
          Logger.info("no specific slug required")
          find(limit, orderBy, request.token.viewerProfileId, request.token, filterBy, sinceId, lastSkippedId)
        case Some(slug) =>
          getBySlug(slug)(request)
      }
    }
  }

  def find(limit: Int, orderBy: String, viewerProfileId: String, token: Token, filterBy: String, sinceId: Option[String], lastSkippedId: Option[String]) = {
    viewerProfileDao.findById(viewerProfileId).flatMap {
      viewerProfile =>
        Logger.info(s"filterby $filterBy")
        filterBy match {
          case "recommends" =>
            val futureStories = storyService.findRecommends(limit, orderBy, viewerProfile, token)
            futureStories.map {
              results =>
                Ok(Json.toJson(TopLevel(stories = Some(Right(results)))))
            }
          case "likes" =>
            val allIds = viewerProfile.likeStoryIds.reverse
            val ids = (lastSkippedId, sinceId) match {
              case (Some(lastSkippedId), None) if allIds.contains(lastSkippedId) =>
                allIds.dropWhile(_ != lastSkippedId).tail.take(limit)
              case (Some(lastSkippedId), Some(sinceId)) if allIds.contains(lastSkippedId) =>
                allIds.dropWhile(_ != lastSkippedId).tail.take(limit).takeWhile(_ != sinceId)
              case (None, Some(sinceId)) =>
                allIds.take(limit).takeWhile(_ != sinceId)
              case (None, None) =>
                allIds.take(limit)
              case _ =>
                List()
            }
            val futureStories = storyDao.getByIds(ids).map(_.sortBy(story => ids.indexOf(story.id)))
            val links: Option[Map[String, String]] = ids.headOption.map(_ => Map("previous" -> s"/stories?filterBy=likes&sinceId=%s".format(ids.head),
              "next" -> s"/stories?filterBy=likes&lastSkippedId=%s".format(ids.last)
            ))
            futureStories.map {
              results =>
                Ok(Json.toJson(TopLevel(stories = Some(Right(results)), links = links)))
            }
          case _ =>
            Future.successful(BadRequest(Error.toTopLevelJson("Incorrect value for filterBy")))
        }

    }
  }

  def getById(id: String) = LoggingAction {
    tokenCheckAction.async { request =>
      storyService.getById(id, request.token).map {
        case None =>
          NotFound(Error.toTopLevelJson(Error("No story for this id %s".format(id))))
        case Some(story) =>
          Ok(Json.toJson(TopLevel(stories = Some(Left(story)))))
      }
    }
  }

  def getBySlug(slug: String)(implicit request: TokenRequest[_]) = storyService.getBySlug(slug, request.token, request.remoteAddress).map {
    case None =>
      NotFound(Error.toTopLevelJson(Error("No story for this slug %s".format(slug))))
    case Some(story) =>
      Ok(Json.toJson(TopLevel(stories = Some(Left(story)))))
  }

  def generateStories() = LoggingAction {
    tokenCheckAction.async(BodyParsers.parse.tolerantJson) { request =>
      val storiesResult = (request.body \ "stories").validate[List[NewStory]]
      storiesResult.fold(
      validationErrors => {
        Future.successful(BadRequest(Error.toTopLevelJson(validationErrors)))
      }, {
        case newStory :: nil =>
          storyDao.generateStory(newStory).flatMap {
            story =>
              Future.successful(Ok(Json.toJson(TopLevel(stories = Some(Right(List(story)))))))
          }
        case _ =>
          Future.successful(BadRequest(Error.toTopLevelJson(s"Create more that 1 story is not supported wet")))
      }
      )
    }
  }
}
