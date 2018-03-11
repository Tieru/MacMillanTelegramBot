package response.entry

import javax.inject.Inject

import model.common.Entry
import repository.entry.EntryRepository
import response.{MessageContext, MessageFormatter}

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

class EntryResponseHandlerImpl @Inject()(repository: EntryRepository)(implicit executionContext: ExecutionContext) extends EntryResponseHandler {

  def handle(args: Seq[String])(implicit messageContext: MessageContext): Unit = {
    val word = args.headOption.getOrElse {
      messageContext.reply("No word submitted. Example: /word magic")
      return
    }

    repository.getEntry(word)
      .transformWith {
        case Success(result) => processRepositoryResponse(result)
        case Failure(_) => Future.successful("Error")
      }
      .map(text => messageContext.reply(text))
  }

  def processRepositoryResponse(entry: Option[Entry])(implicit messageContext: MessageContext): Future[String] = Future {
    val text = entry match {
      case Some(value) => MessageFormatter.formatEntry(value)
      case _ => "Nothing found"
    }

    text
  }

}
