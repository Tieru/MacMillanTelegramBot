package response

import dictionary.Api
import info.mukel.telegrambot4s.api.{BotBase, Extractors}
import info.mukel.telegrambot4s.methods.AnswerInlineQuery
import info.mukel.telegrambot4s.models.{InlineQuery, InlineQueryResultArticle}

class BotInlineQueryContext(bot: BotBase)(implicit inlineQuery: InlineQuery) extends InlineQueryContext {

  def request(results: Seq[InlineQueryResultArticle]): Unit = {
    val offset = Extractors.Int.unapply(inlineQuery.offset).getOrElse(0)
    val nextOffset = Option((offset + Api.defaultCount).toString)

    val answerQuery = AnswerInlineQuery(inlineQuery.id, results, nextOffset = nextOffset)
    bot.request(answerQuery)
  }

}
