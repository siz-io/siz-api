import java.util.Date

import models._
import org.junit.runner.RunWith
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner
import play.api.libs.json.{JsObject, JsString}
import play.api.test.Helpers._
import play.api.test.{FakeRequest, WithApplication}

@RunWith(classOf[JUnitRunner])
class StoriesSpec extends Specification {

  "Stories" should {
    "Retrieve a unlisted story" in new WithApplication{
      val storyId = "14339467864855da8fe28615"
      val newStory = Story(boxes = List(), creationDate = new Date(), id = storyId,
        slug = "pepper-spray", source = Source("9dLmdVDjg1w","youtube",Some(1592000)), picture = Image("http://img.youtube.com/vi/9dLmdVDjg1w/0.jpg"), title = "Pepper Spray",
        tags = List("short-films"),
        privacy = "unlisted")
      Story.collection.insert(newStory)


      val createdToken = contentAsJson(route(FakeRequest(POST, "/tokens").withJsonBody(JsObject(Seq()))).get)

      val retrievedStory = route(FakeRequest(GET, s"/stories/$storyId").
        withHeaders(("X-Access-Token",(createdToken \ "tokens" \ "id").as[String]))).get

      status(retrievedStory) must equalTo(CREATED)
      contentType(retrievedStory) must  beSome.which(_ == "application/json")
      val jsonEvent = contentAsJson(retrievedStory) \ "stories"
      jsonEvent \ "id" mustEqual JsString(storyId)
    }
  }
}
