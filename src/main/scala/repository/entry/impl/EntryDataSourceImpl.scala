package repository.entry.impl

import dictionary.Api
import play.api.libs.json.Json
import repository.entry.RawEntry

import scala.concurrent.{ExecutionContext, Future}

class EntryDataSourceImpl(clientWrapper: Api)(implicit ec: ExecutionContext) {

  def getEntry(entry: String): Future[Option[RawEntry]] = {
    clientWrapper.getEntry(entry)
      .map(raw => Json.parse(raw).validate[RawEntry].asOpt)
  }

}
