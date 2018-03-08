package repository.entry

import scala.concurrent.Future

trait EntryDataSource {

  def getEntry(entry: String): Future[Option[RawEntry]]

}


