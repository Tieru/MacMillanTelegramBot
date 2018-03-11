package app

import com.google.inject.Guice
import di.ApplicationModule
import info.mukel.telegrambot4s.api.declarative.Commands
import info.mukel.telegrambot4s.api.{Polling, TelegramBot}
import response.BotMessageContext
import response.entry.EntryResponseHandler


object DictionaryBot extends TelegramBot with Polling with Commands {

  import net.codingwell.scalaguice.InjectorExtensions._
  private val injector = Guice.createInjector(new ApplicationModule())

  override def token: String = scala.util.Properties
    .envOrNone("BOT_TOKEN")
    .getOrElse(throw new IllegalArgumentException("No Bot token found"))

    onCommand("/word") { implicit msg =>
      withArgs { args =>
        val handler = injector.instance[EntryResponseHandler]
        handler.handle(args)(new BotMessageContext(this))
      }
    }

  println("Bot started successfully")

}
