package repository.entry


import model.common.{Dictionary, Entry}

import scala.concurrent.Future

trait EntryRepository {

  def getEntry(search: String, dictionary: Dictionary.Type = Dictionary.American): Future[Option[Entry]]

}


