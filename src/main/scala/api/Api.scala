package api

import model.common.Dictionary
import model.common.Dictionary.Type
import repository.entry.RawEntry
import repository.search.SearchResults

import scala.concurrent.Future

trait Api {

  def getEntry(entryId: String, dictionaryCode: Type = Dictionary.American): Future[RawEntry]

  def search(search: String,
             offset: Int,
             count: Int = Api.defaultSearchCount,
             dictionaryCode: Type = Dictionary.American): Future[SearchResults]

  def searchFirst(search: String, dictionaryCode: Type = Dictionary.American): Future[RawEntry]

}

object Api {

  val defaultSearchCount = 5

}
