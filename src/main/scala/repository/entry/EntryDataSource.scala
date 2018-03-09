package repository.entry

import repository.entry.model.RawEntry

import scala.concurrent.Future

trait EntryDataSource {

  def getEntry(entry: String): Future[Option[RawEntry]]

}


