package repository.entry

import api.Api
import client.SkPublishAPIException
import model.common.{Dictionary, Entry, EntryContent}
import org.scalamock.scalatest.MockFactory
import org.scalatest.FlatSpec
import repository.entry.impl.{EntryCache, EntryRepositoryImpl}
import sun.jvm.hotspot.utilities.AssertionFailure
import tools.RawResourceLoader

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import scala.xml.XML

class EntryRepositorySpec extends FlatSpec with MockFactory with RawResourceLoader {

  private val clientWrapper = mock[Api]
  private val cache = mock[EntryCache]
  private val repository = new EntryRepositoryImpl(clientWrapper, cache)

  "Repository" should "try to get results from cache first for particular entry request" in {

    val entryId = "amazing123456"
    val dictionaryCode = "american"

    val entryContent = EntryContent.fromXml(XML.loadString(rawResource("raw/entry/entryContentAmazing.xml")))
    val entry = Entry(entryId, entryId, Seq(), dictionaryCode, entryContent)

    (cache.get _).expects(entryId).returning(Option(entry))
    (clientWrapper.getEntry _).expects(entryId, Dictionary.American)/*.returning(Future.successful(*))*/.never()

    val result = Await.result(repository.getEntry(entryId), Duration.Inf).get
    assert(result == entry)
  }

  it should "take request data from server when no cached value available for entry" in {
    val requestedWord = "amazing"
    val mockResponse = TestEntities.rawEntryFromResources("raw/entry/entryAmazing.json")
    (cache.get _).expects(*).returning(None)
    (clientWrapper.getEntry _).expects(requestedWord, Dictionary.American).returning(Future.successful(mockResponse))
    (cache.store _).expects(*)

    val result = Await.result(repository.getEntry(requestedWord), Duration.Inf).getOrElse(throw new AssertionFailure("Repository should return a value"))
    assert(result.dictionaryCode == Dictionary.American.toString)
    assert(result.entryLabel == requestedWord)
    assert(result.topics.isEmpty)
    assert(result.entryContent != null)
    assert(result.entryContent.id == requestedWord)
    assert(result.entryId == requestedWord)
  }

  it should "cache data for successfully fetched results" in {
    val requestedWord = "amazing"
    val mockResponse = TestEntities.rawEntryFromResources("raw/entry/entryAmazing.json")

    inSequence {
      (cache.get _).expects(requestedWord).returning(None)
      (clientWrapper.getEntry _).expects(requestedWord, Dictionary.American).returning(Future.successful(mockResponse))
      (cache.store _).expects(*)
      (cache.get _).expects(requestedWord).returning(Option(TestEntities.createEntry(requestedWord)))
      (clientWrapper.getEntry _).expects(requestedWord, Dictionary.American).never()
    }

    Await.result(repository.getEntry(requestedWord), Duration.Inf).getOrElse(throw new AssertionFailure("Repository should return a value"))

    val result = Await.result(repository.getEntry(requestedWord), Duration.Inf)

    assert(result.nonEmpty)
  }

  it should "get entries from wrapper" in {

    val requestedWord = "amazing"
    val mockResponse = TestEntities.rawEntryFromResources("raw/entry/entryAmazing.json")
    (clientWrapper.searchFirst _).expects(requestedWord, Dictionary.American).returning(Future.successful(mockResponse))

    val result = Await.result(repository.searchFirst(requestedWord), Duration.Inf).getOrElse(throw new AssertionFailure("Repository should return a value"))
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
    (clientWrapper.searchFirst _).expects(requestedWord, Dictionary.American).returning(Future.failed(exception))

    val result = Await.result(repository.searchFirst(requestedWord), Duration.Inf)

    assert(result.isEmpty)
  }
}
