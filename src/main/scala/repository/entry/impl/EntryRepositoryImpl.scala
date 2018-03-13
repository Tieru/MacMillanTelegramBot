package repository.entry.impl

import javax.inject.Inject

import client.SkPublishAPIException
import dictionary.Api
import model.common.{Dictionary, Entry, EntryContent}
import play.api.libs.json.Json
import repository.entry.{EntryRepository, RawEntry}

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}
import scala.xml.XML

class EntryRepositoryImpl @Inject()(clientWrapper: Api, cache: EntryCache)(implicit ec: ExecutionContext) extends EntryRepository {

  def getEntry(entry: String, dictionary: Dictionary.Type = Dictionary.American): Future[Option[Entry]] = {
    val cached: Entry = cache.get(entry).getOrElse {
      return loadEntry(entry, dictionary)
    }
    Future.successful(Option[Entry](cached))
  }

  private def loadEntry(entry: String, dictionary: Dictionary.Type = Dictionary.American): Future[Option[Entry]] = {
    clientWrapper.getEntry(entry, dictionary)
      .transformWith {
        case Success(raw) => Future.successful(rawEntryToEntry(raw))
        case Failure(cause) => cause match {
          case e: SkPublishAPIException => handleApiFailure(e)
          case _ => Future.failed(cause)
        }
      }
      .map { result =>
        cacheResultIfPossible(result)
        result
      }
  }

  private def cacheResultIfPossible(entry: Option[Entry]): Unit = {
    val result = entry.getOrElse {
      return
    }
    cache.store(result)
  }

  def searchFirst(entry: String, dictionary: Dictionary.Type = Dictionary.American): Future[Option[Entry]] = {
    clientWrapper.searchFirst(entry, dictionary)
      .transformWith {
        case Success(raw) => Future.successful(rawEntryToEntry(raw))
        case Failure(cause) => cause match {
          case e: SkPublishAPIException => handleApiFailure(e)
          case _ => Future.failed(cause)
        }
      }
  }

  private def handleApiFailure[T](e: SkPublishAPIException): Future[Option[T]] = {
    e.getStatusCode match {
      case 404 => Future.successful(None)
      case _ => Future.failed(e)
    }
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
