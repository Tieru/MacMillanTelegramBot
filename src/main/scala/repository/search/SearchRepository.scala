package repository.search

import dictionary.Api
import model.common.Dictionary

import scala.concurrent.Future

trait SearchRepository {

  def search(query: String, offset: Int, count: Int = Api.defaultCount, dictionary: Dictionary.Type = Dictionary.American): Future[Option[SearchResults]]

}
