package model

case class NothingFound() extends BotResponse {
  override val response: String = "Nothing found"
}
