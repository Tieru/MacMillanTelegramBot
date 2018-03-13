package response.search

import javax.inject.Inject

import com.typesafe.scalalogging.Logger
import info.mukel.telegrambot4s.methods.ParseMode
import info.mukel.telegrambot4s.models.{InlineQueryResultArticle, InputTextMessageContent}
import repository.search.{SearchRepository, SearchResults}
import response.InlineQueryContext

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

class SearchResponseHandlerImpl @Inject()(repository: SearchRepository)(implicit ec: ExecutionContext) extends SearchResponseHandler {

  private val logger = Logger(classOf[SearchResponseHandlerImpl])

  def handle(query: String, offset: Int)(context: InlineQueryContext): Unit = {

    logger.info(f"Inline query: $query, offset: $offset")

    if (query.length < 2) {
      context.request(Seq())
      return
    }

    repository.search(query.toLowerCase, offset)
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

  private def processRepositoryResponse(search: Option[SearchResults]): Seq[InlineQueryResultArticle] = {

    val searchResults = search.getOrElse {
      return Seq()
    }

    for {
      result <- searchResults.results
    } yield {
      val content = InputTextMessageContent("/word " + result.entryId, Option(ParseMode.Markdown))
      InlineQueryResultArticle(result.entryId, result.entryLabel, content)
    }

  }

}
