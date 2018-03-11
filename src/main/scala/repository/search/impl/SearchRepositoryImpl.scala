package repository.search.impl

import javax.inject.Inject

import dictionary.Api
import model.common.Dictionary
import play.api.libs.json.Json
import repository.search.{SearchRepository, SearchResults}

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

class SearchRepositoryImpl @Inject()(api: Api)(implicit ec: ExecutionContext) extends SearchRepository {

  def search(query: String, offset: Int, dictionary: Dictionary.Type = Dictionary.American): Future[Option[SearchResults]] = {
    api.search(query, offset, dictionary=dictionary)
      .transformWith {
        case Success(raw) => Future.successful(parseSearchResult(raw))
        case Failure(cause) => cause match {
          case _ => Future.successful(None)
        }
      }
  }

  def parseSearchResult(raw: String): Option[SearchResults] = {
    val results = Json.parse(raw).validate[SearchResults].asOpt
    results match {
      case None => None
      case Some(value) => Option(value)
    }
  }

}
