package repository.search.impl

import javax.inject.Inject

import api.Api
import model.common.Dictionary
import repository.search.{SearchRepository, SearchResults}

import scala.concurrent.{ExecutionContext, Future}

class SearchRepositoryImpl @Inject()(api: Api)(implicit ec: ExecutionContext) extends SearchRepository {

  def search(query: String,
             offset: Int,
             count: Int = Api.defaultSearchCount,
             dictionary: Dictionary.Type = Dictionary.American): Future[SearchResults] = {
    api.search(query, offset, count, dictionary)
  }

}
