package app

import com.google.inject.Guice
import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.Logger
import di.ApplicationModule
import info.mukel.telegrambot4s.api.declarative.Commands
import info.mukel.telegrambot4s.api.{Extractors, Polling, TelegramBot}
import info.mukel.telegrambot4s.models.InlineQuery
import response.entry.EntryResponseHandler
import response.search.SearchResponseHandler
import response.{BotInlineQueryContext, BotMessageContext, InlineQueryContext, MessageContext}


object DictionaryBot extends TelegramBot with Polling with Commands {

  private val botLogger = Logger("Bot")

  import net.codingwell.scalaguice.InjectorExtensions._

  private val injector = Guice.createInjector(new ApplicationModule())

  override def token: String = ConfigFactory.load().getString("bot.token")

  onCommand("/word") { implicit msg =>
    withArgs { args =>
      val handler = injector.instance[EntryResponseHandler]
      implicit val context: MessageContext = new BotMessageContext(this)
      handler.handle(args)
    }
  }

  override def receiveInlineQuery(inlineQuery: InlineQuery): Unit = {
    super.receiveInlineQuery(inlineQuery)

    val query = inlineQuery.query
    val offset = Extractors.Int.unapply(inlineQuery.offset).getOrElse(0)

    implicit val context: InlineQueryContext = new BotInlineQueryContext(this)(inlineQuery)
    val handler = injector.instance[SearchResponseHandler]
    handler.handle(query, offset)(context)
  }

  botLogger.info("Bot started successfully")
}
