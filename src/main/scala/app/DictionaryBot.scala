package app

import java.net.{InetSocketAddress, Proxy}

import com.google.inject.Guice
import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.Logger
import di.ApplicationModule
import info.mukel.telegrambot4s.api._
import info.mukel.telegrambot4s.api.declarative.Commands
import info.mukel.telegrambot4s.clients.{AkkaHttpClient, ScalajHttpClient}
import info.mukel.telegrambot4s.models.InlineQuery
import response.entry.EntryResponseHandler
import response.search.SearchResponseHandler
import response.{BotInlineQueryContext, BotMessageContext, InlineQueryContext, MessageContext}

object DictionaryBot extends AkkaTelegramBot with Polling with Commands {

  private val botLogger = Logger("Bot")

  import net.codingwell.scalaguice.InjectorExtensions._

  private val injector = Guice.createInjector(new ApplicationModule())

  override def token: String = ConfigFactory.load().getString("bot.token")

  override val client: RequestHandler = {
    val token = ConfigFactory.load().getString("bot.token")
    val configs = ConfigFactory.load()
    if (configs.hasPath("bot.useProxy") && configs.getBoolean("bot.useProxy")) {
      new ScalajHttpClient(token, buildProxySettings())
    } else {
      new AkkaHttpClient(token)
    }
  }

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

  private def buildProxySettings(): Proxy = {
    val configs = ConfigFactory.load()
    var host = configs.getString("bot.proxyHost")

    if (configs.hasPath("bot.proxyUser")) {
      val proxyUser = configs.getString("bot.proxyUser")
      val proxyPassword = configs.getString("bot.proxyPassword")
      host = s"$proxyUser:$proxyPassword@$host"
    }

    val port = configs.getInt("bot.proxyPort")
    new Proxy(Proxy.Type.HTTP, InetSocketAddress.createUnresolved(host, port))
  }
}
