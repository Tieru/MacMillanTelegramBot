package repository

import dictionary.Api
import org.scalamock.scalatest.MockFactory
import org.scalatest.FlatSpec
import repository.entry.impl.EntryDataSourceImpl
import sun.jvm.hotspot.utilities.AssertionFailure
import tools.RawResourceLoader

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}


class EntryDataSourceSpec extends FlatSpec with MockFactory with RawResourceLoader {

  private val clientWrapper = mock[Api]
  private val entryDataSource = new EntryDataSourceImpl(clientWrapper)

  "Data source" should "get entries from wrapper" in {

    val requestedWord = "amazing"
    val mockResponse = rawResource("raw/entryAmazing.json")
    (clientWrapper.getEntry _).expects(requestedWord, Api.AMERICAN, Api.XML)
      .returning(Future.successful(mockResponse))

    val result = Await.result(entryDataSource.getEntry(requestedWord), Duration.Inf).getOrElse(throw new AssertionFailure("DataSource should return a value"))
    assert(result.dictionaryCode == Api.AMERICAN)
    assert(result.format == Api.XML)
    assert(result.entryLabel == requestedWord)
    assert(result.topics.isEmpty)
    assert(!result.entryContent.isEmpty)
    assert(result.entryId == requestedWord)
  }

}
