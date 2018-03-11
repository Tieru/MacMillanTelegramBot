package repository.search

import play.api.libs.json.{Json, OFormat}

case class SearchResults(pageNumber: Int, dictionaryCode: String, resultNumber: Int, currentPageIndex: Int, results: Seq[SearchResult])

object SearchResults {
  implicit val format: OFormat[SearchResults] = Json.format[SearchResults]
}