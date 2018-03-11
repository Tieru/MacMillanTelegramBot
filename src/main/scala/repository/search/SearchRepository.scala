package repository.search

import model.common.Dictionary

import scala.concurrent.Future

trait SearchRepository {

  def search(query: String, offset: Int, dictionary: Dictionary.Type = Dictionary.American): Future[Option[SearchResults]]

}
