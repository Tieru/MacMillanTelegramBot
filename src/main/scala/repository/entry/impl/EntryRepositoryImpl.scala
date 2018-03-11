package repository.entry.impl

import dictionary.Api
import model.common.{Dictionary, Entry, EntryContent}
import play.api.libs.json.Json
import repository.entry.{EntryRepository, RawEntry}

import scala.concurrent.{ExecutionContext, Future}
import scala.xml.XML

class EntryRepositoryImpl(clientWrapper: Api)(implicit ec: ExecutionContext) extends EntryRepository{

  def getEntry(entry: String, dictionary: Dictionary.Type = Dictionary.American): Future[Option[Entry]] = {
    clientWrapper.getEntry(entry)
      .map(raw => rawEntryToEntry(raw))
  }

  private def rawEntryToEntry(raw: String): Option[Entry] = {
    val rawEntry = Json.parse(raw).validate[RawEntry].asOpt
    rawEntry match {
      case None => None
      case Some(value) => Option(parseEntryContent(value))
    }
  }

  private def parseEntryContent(rawEntry: RawEntry): Entry = {
    val entryContent = EntryContent.fromXml(XML.loadString(rawEntry.entryContent))
    Entry(rawEntry.entryId, rawEntry.entryLabel, rawEntry.topics, rawEntry.entryUrl, rawEntry.dictionaryCode, entryContent)
  }

}
