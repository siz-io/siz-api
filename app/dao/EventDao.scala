package dao

import javax.inject.{Inject, Singleton}
import models.{NewEvent, Event}
import play.modules.reactivemongo.ReactiveMongoApi
import play.modules.reactivemongo.json.collection.JSONCollection
import play.api.libs.json._
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import reactivemongo.api.indexes.{IndexType, Index}
import reactivemongo.bson.BSONObjectID



/**
 * Created by fred on 16/07/15.
 */
@Singleton
class EventDao @Inject()(val reactiveMongoApi: ReactiveMongoApi) {

  implicit val eventRead = Json.reads[Event]
  implicit val eventWrite = Json.writes[Event]

  lazy val db = reactiveMongoApi.db

  def collection: JSONCollection = db.collection[JSONCollection]("events")

  // migrate data before app startup and after injection
  updateDB

  def updateDB = {
    collection.indexesManager.ensure(Index(Seq("storyId" -> IndexType.Ascending, "viewerProfileId" -> IndexType.Ascending), name = Some("storyIdViewerProfileIdUniqueIndex"), unique = true, sparse = true))
  }

  def newEventToEvent(newEvent: NewEvent, viewerProfileId: String, tags: List[String]): Event = Event(newEvent.storyId, newEvent._type, tags, viewerProfileId, BSONObjectID.generate.stringify)

  def addEvent(event: Event) = collection.insert(event)
}