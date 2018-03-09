package repository.entry

import repository.entry.model.Entry

import scala.concurrent.Future

trait EntryRepository {

  def getEntry(search: String): Future[Option[Entry]]

}


