package repository.entry

import model.common.{Entry, EntryContent}
import play.api.libs.json.Json
import repository.search.SearchResults
import tools.RawResourceLoader

import scala.xml.XML


object TestEntities extends RawResourceLoader {

  def createEntry(entryId: String = "amazing"): Entry = {
    val entryUrl = "url"
    val dictionaryCode = "american"

    val entryContent = EntryContent.fromXml(XML.loadString(rawResource("raw/entry/entryContentAmazing.xml")))
    Entry(entryId, entryId, Seq(), entryUrl, dictionaryCode, entryContent)
  }

  def entryFromResources(path: String): Entry = {
    val rawEntry = rawEntryFromResources(path)
    val entryContent = EntryContent.fromXml(XML.loadString(rawEntry.entryContent))
    Entry(entryContent.id, entryContent.id, Seq(), "url", "american", entryContent)
  }

  def rawEntryFromResources(path: String): RawEntry = {
    Json.parse(rawResource(path)).validate[RawEntry].get
  }

  def searchResultsFromResources(path: String): SearchResults = {
    Json.parse(rawResource(path)).validate[SearchResults].get
  }

}
