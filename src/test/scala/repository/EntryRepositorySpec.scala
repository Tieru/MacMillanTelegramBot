package repository

import dictionary.Api
import entity.{NothingFound, EntryResponse}
import org.scalamock.scalatest.MockFactory
import org.scalatest.FlatSpec
import repository.entry.impl.EntryRepositoryImpl
import repository.entry.EntryDataSource
import repository.entry.model.RawEntry

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

class EntryRepositorySpec extends FlatSpec with MockFactory {

  private val dataSource = stub[EntryDataSource]
  private val repository = new EntryRepositoryImpl(dataSource)

  "A Repository" should "return WordEntry when it finds a word" in {

    val desiredWord = "miracle"
    val expectedUrl = "url"
    val expectedContent = "content"
    val dataSourceResult = Option(RawEntry(desiredWord, desiredWord, Array.empty, expectedUrl, Api.XML, Api.AMERICAN, expectedContent))
    (dataSource.getEntry _).when(desiredWord).returning(Future.successful(dataSourceResult))

    val result = Await.result(repository.getEntry(desiredWord), Duration.Inf)
    (dataSource.getEntry _).verify(desiredWord)
    assert(result.isInstanceOf[EntryResponse])
    assert(result.asInstanceOf[EntryResponse].word === desiredWord)
  }

  it should "return NothingFound when there is no words found" in {
    (dataSource.getEntry _).when(*).returning(Future.successful(None))

    val result = Await.result(repository.getEntry("whatever"), Duration.Inf)
    assert(result.isInstanceOf[NothingFound])
  }

}
