package repository.search

import play.api.libs.json.{Json, OFormat}

case class SearchResult(entryLabel: String, entryId: String)

object SearchResult {
  implicit val format: OFormat[SearchResult] = Json.format[SearchResult]
}
