package response.search

import response.InlineQueryContext

trait SearchResponseHandler {

  def handle(query: String, offset: Int)(context: InlineQueryContext): Unit

}
