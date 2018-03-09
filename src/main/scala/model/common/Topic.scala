package model.common

import play.api.libs.json.{Json, OFormat}

case class Topic(id: Option[String])

object Topic {
  implicit val format: OFormat[Topic] = Json.format[Topic]
}
