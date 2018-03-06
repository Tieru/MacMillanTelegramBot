import info.mukel.telegrambot4s.api.declarative.Commands
import info.mukel.telegrambot4s.api.{Polling, TelegramBot}

object DictionaryBot extends TelegramBot with Polling with Commands {

  override def token: String = scala.util.Properties
    .envOrNone("BOT_TOKEN")
    .getOrElse(throw new IllegalArgumentException("No Bot token found"))


  println("Bot started successfully")

}
