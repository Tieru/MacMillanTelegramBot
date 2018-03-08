package repository.entry

import model.{BotResponse, NothingFound, WordEntry}

import scala.concurrent.{ExecutionContext, Future}

trait EntryRepository {

  def getEntry(search: String): Future[BotResponse]

}


