package response

import dictionary.Api
import info.mukel.telegrambot4s.methods.ParseMode.ParseMode
import info.mukel.telegrambot4s.models.ReplyMarkup
import model.common.Dictionary
import org.scalamock.scalatest.MockFactory
import org.scalatest.concurrent.Eventually
import org.scalatest.{FlatSpec, Matchers}
import repository.entry.EntryRepository
import repository.entry.impl.{EntryCacheImpl, EntryRepositoryImpl}
import response.entry.EntryResponseHandlerImpl
import tools.RawResourceLoader

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class EntryResponseHandlerSpec extends FlatSpec with MockFactory with RawResourceLoader with Eventually with Matchers {

  private val clientWrapper = mock[Api]
  private val cache = new EntryCacheImpl()
  private val entryRepository = new EntryRepositoryImpl(clientWrapper, cache)

  private val mockRepository = mock[EntryRepository]
  private val entryResponseHandler = new EntryResponseHandlerImpl(entryRepository)
  private val messageContext = mock[MessageContext]

  "Entry response handler" should "format response from server" in {
    val entryWord = "amazing"
    val expectedResponse = rawResource("response/amazingResponse.txt")

    val rawInfo = rawResource("raw/entry/entryAmazing.json")
    (clientWrapper.searchFirst _).expects(entryWord, Dictionary.American, Api.XML).returning(Future.successful(rawInfo))

    val testResult = new TestResult[String]()

    var handler = (text: String,
                   parseMode: Option[ParseMode],
                   disableWebPagePreview: Option[Boolean],
                   disableNotification: Option[Boolean],
                   replyToMessageId: Option[Int],
                   replyMarkup: Option[ReplyMarkup]) => {

      assert(parseMode.isEmpty)
      assert(disableWebPagePreview.isEmpty)
      assert(disableNotification.isEmpty)
      assert(replyToMessageId.isEmpty)
      assert(replyMarkup.isEmpty)

      testResult.result = Option(text)

      ()
    }

    (messageContext.reply _).expects(*, *, *, *, *, *).onCall(handler)

    entryResponseHandler.handle(Seq(entryWord))(messageContext)

    //todo find a way to test this
    Thread.sleep(500)
    testResult.result.get shouldBe expectedResponse
  }

}

private class TestResult[T] {

  var result: Option[T] = None

}