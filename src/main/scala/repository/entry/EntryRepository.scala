package repository.entry

import entity.BotResponse

import scala.concurrent.Future

trait EntryRepository {

  def getEntry(search: String): Future[BotResponse]

}


