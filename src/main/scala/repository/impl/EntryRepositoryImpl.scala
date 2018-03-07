package repository.impl

import model.{BotResponse, NothingFound}
import repository.EntryRepository
import repository.source.EntryDataSource

class EntryRepositoryImpl(val dataSource: EntryDataSource) extends EntryRepository {

  def getEntry(search: String): BotResponse = {
    val possibleWord = dataSource.findWord(search)
    possibleWord match {
      case Some(word) => word
      case None => NothingFound()
    }
  }

}
