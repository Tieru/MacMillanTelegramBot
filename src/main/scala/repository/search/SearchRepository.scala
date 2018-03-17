package repository.search

import api.Api
import model.common.Dictionary

import scala.concurrent.Future

trait SearchRepository {

  def search(query: String,
             offset: Int,
             count: Int = Api.defaultSearchCount,
             dictionary: Dictionary.Type = Dictionary.American): Future[SearchResults]

}
