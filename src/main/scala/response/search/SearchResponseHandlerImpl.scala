package response.search

import javax.inject.Inject

import com.typesafe.scalalogging.Logger
import info.mukel.telegrambot4s.methods.ParseMode
import info.mukel.telegrambot4s.models.{InlineQueryResultArticle, InputTextMessageContent}
import model.common.Entry
import response.{InlineQueryContext, MessageFormatter}

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

class SearchResponseHandlerImpl @Inject()(entriesSearchFetcher: EntriesSearchFetcher)(implicit ec: ExecutionContext) extends SearchResponseHandler {

  private val logger = Logger(classOf[SearchResponseHandlerImpl])

  def handle(query: String, offset: Int)(context: InlineQueryContext): Unit = {

    logger.info(f"Inline query: $query, offset: $offset")

    if (query.length < 2) {
      context.request(Seq())
      return
    }

    entriesSearchFetcher.findEntries(query.toLowerCase(), offset)
      .transformWith {
        case Success(result) => Future.successful(processRepositoryResponse(result))
        case Failure(cause) =>
          logger.error("Failed to handle inline request command", cause)
          Future.successful(Seq[InlineQueryResultArticle]())
      }
      .map(results => {
        val resultsCount = results.length
        logger.info(s"Handled inline request '$query' with $resultsCount results")
        context.request(results)
      })
  }

  private def processRepositoryResponse(entries: Seq[Entry]): Seq[InlineQueryResultArticle] = {

    for {
      entry <- entries
    } yield {
      val content = InputTextMessageContent(MessageFormatter.formatEntry(entry), Option(ParseMode.Markdown))
      InlineQueryResultArticle(entry.entryId, entry.entryLabel, content)
    }

  }

}
