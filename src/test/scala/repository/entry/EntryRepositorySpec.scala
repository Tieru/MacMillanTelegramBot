package repository.entry

import client.SkPublishAPIException
import dictionary.Api
import model.common.Dictionary
import org.scalamock.scalatest.MockFactory
import org.scalatest.FlatSpec
import repository.entry.impl.EntryRepositoryImpl
import sun.jvm.hotspot.utilities.AssertionFailure
import tools.RawResourceLoader

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

class EntryRepositorySpec extends FlatSpec with MockFactory with RawResourceLoader {

  private val clientWrapper = mock[Api]
  private val repository = new EntryRepositoryImpl(clientWrapper)

  "Repository" should "get entries from wrapper" in {

    val requestedWord = "amazing"
    val mockResponse = rawResource("raw/entry/entryAmazing.json")
    (clientWrapper.getEntry _).expects(requestedWord, Dictionary.American, Api.XML).returning(Future.successful(mockResponse))

    val result = Await.result(repository.getEntry(requestedWord), Duration.Inf).getOrElse(throw new AssertionFailure("Repository should return a value"))
    assert(result.dictionaryCode == Dictionary.American.toString)
    assert(result.entryLabel == requestedWord)
    assert(result.topics.isEmpty)
    assert(result.entryContent != null)
    assert(result.entryContent.id == requestedWord)
    assert(result.entryId == requestedWord)
  }

  it should "handle 404 status error from wrapper and return 'None'" in {
    val requestedWord = "amazing"
    val exception = new SkPublishAPIException(404, "")
    (clientWrapper.getEntry _).expects(requestedWord, Dictionary.American, Api.XML).returning(Future.failed(exception))

    val result = Await.result(repository.getEntry(requestedWord), Duration.Inf)

    assert(result.isEmpty)
  }
}
