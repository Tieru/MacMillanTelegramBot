package repository.search

import dictionary.Api
import model.common.Dictionary
import org.scalamock.scalatest.MockFactory
import org.scalatest.FlatSpec
import repository.search.impl.SearchRepositoryImpl
import tools.RawResourceLoader

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

class SearchRepositorySpec extends FlatSpec with MockFactory with RawResourceLoader {

  private val clientWrapper = mock[Api]
  private val repository = new SearchRepositoryImpl(clientWrapper)

  "Repository" should "retrieve results from api" in {
    val searchQuery = "power"
    val pageIndex = 1
    val count = 5

    val rawResponse = rawResource("raw/search/power_1_5.json")
    (clientWrapper.search _).expects(searchQuery, pageIndex, count, Dictionary.American).returning(Future.successful(rawResponse))

    val result = Await.result(repository.search(searchQuery, pageIndex), Duration.Inf).get
    assert(result.currentPageIndex == 1)
    assert(result.pageNumber == 9)
    assert(result.dictionaryCode == Dictionary.American.toString)
    assert(result.resultNumber == 44)

    assert(result.results.lengthCompare(count) == 0)
    val headSearchResult = result.results.head
    assert(headSearchResult.entryLabel == "power  noun")
    assert(headSearchResult.entryUrl == "https://www.macmillandictionary.com/dictionary/american/power_1")
    assert(headSearchResult.entryId == "power_1")
  }

}
