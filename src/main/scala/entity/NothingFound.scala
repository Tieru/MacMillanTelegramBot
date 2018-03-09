package entity

case class NothingFound() extends BotResponse {
  override val response: String = "Nothing found"
}
