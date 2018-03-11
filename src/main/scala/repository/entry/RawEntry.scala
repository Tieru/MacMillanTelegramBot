package repository.entry

import model.common.Topic
import play.api.libs.json.{Json, OFormat}

case class RawEntry
(
  entryId: String,
  entryLabel: String,
  topics: Array[Topic],
  entryUrl: String,
  format: String,
  dictionaryCode: String,
  entryContent: String
)

object RawEntry {
  implicit val format: OFormat[RawEntry] = Json.format[RawEntry]
}