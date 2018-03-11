package response

import info.mukel.telegrambot4s.models.InlineQueryResultArticle

trait InlineQueryContext {

  def request(query: Seq[InlineQueryResultArticle]): Unit

}
