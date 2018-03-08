package repository.entry.impl

import model.{BotResponse, NothingFound, WordEntry}
import repository.entry.EntryDataSource

import scala.concurrent.{ExecutionContext, Future}

class EntryRepositoryImpl(val dataSource: EntryDataSource)(implicit ec: ExecutionContext) {

  def getEntry(search: String): Future[BotResponse] = {
    dataSource.getEntry(search)
      .map {
        case Some(word) => WordEntry(word.entryLabel)
        case None => NothingFound()
      }
  }

}
