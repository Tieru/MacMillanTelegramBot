package response

import dictionary.Api
import model.common.Dictionary
import org.scalamock.scalatest.MockFactory
import org.scalatest.FlatSpec
import play.api.libs.json.Json
import repository.entry.{CommonEntries, EntryRepository}
import repository.search.{SearchRepository, SearchResults}
import response.search.EntriesSearchResultsFetcherImpl
import tools.RawResourceLoader

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

class EntriesSearchFetcherSpec extends FlatSpec with MockFactory with RawResourceLoader {

  private val searchRepository = mock[SearchRepository]
  private val entryRepository = mock[EntryRepository]

  private val entriesSearchFetcher = new EntriesSearchResultsFetcherImpl(searchRepository, entryRepository)

  "Fetcher" should "search for entries and prefetch their full content" in {

    val query = "power"
    val offset = 0

    val searchResponse = rawResource("raw/search/power_1_5.json")
    val searchResult = Json.parse(searchResponse).validate[SearchResults].asOpt
    val searchEntries = searchResult.get.results

    val entryPower1 = Option(CommonEntries.fromResourceFile("raw/entry/entryPower_1.json"))
    val entryPower2 = Option(CommonEntries.fromResourceFile("raw/entry/entryPower_2.json"))
    val entryPower3 = Option(CommonEntries.fromResourceFile("raw/entry/entryPower_3.json"))
    val entryPowerUp = Option(CommonEntries.fromResourceFile("raw/entry/entryPower-up.json"))
    val entryAirPower = Option(CommonEntries.fromResourceFile("raw/entry/entryAir-power.json"))

    inSequence {
      (searchRepository.search _).expects(query, offset, Api.defaultCount, Dictionary.American).returning(Future.successful(searchResult))
      (entryRepository.getEntry _).expects(searchEntries.head.entryId, Dictionary.American).returning(Future.successful(entryPower1))
      (entryRepository.getEntry _).expects(searchEntries.apply(1).entryId, Dictionary.American).returning(Future.successful(entryPower2))
      (entryRepository.getEntry _).expects(searchEntries.apply(2).entryId, Dictionary.American).returning(Future.successful(entryPower3))
      (entryRepository.getEntry _).expects(searchEntries.apply(3).entryId, Dictionary.American).returning(Future.successful(entryPowerUp))
      (entryRepository.getEntry _).expects(searchEntries.apply(4).entryId, Dictionary.American).returning(Future.successful(entryAirPower))
    }

    val results = Await.result(entriesSearchFetcher.findEntries(query, offset), Duration.Inf)

    assert(results.lengthCompare(5) == 0)

    assert(results.head.entryId == "power_1")
    assert(results.apply(1).entryId == "power_2")
    assert(results.apply(2).entryId == "power_3")
    assert(results.apply(3).entryId == "power-up")
    assert(results.apply(4).entryId == "air-power")
  }

}


class SleepyObject(result: Int) {

  def sleep(): Future[Int] = Future[Int] {
    Thread.sleep(1000)
    result
  }

}