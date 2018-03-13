package repository.entry

import model.common.{Entry, EntryContent}
import tools.RawResourceLoader

import scala.xml.XML


object CommonEntries extends RawResourceLoader {

  def createEntry(entryId: String = "amazing"): Entry = {
    val entryUrl = "url"
    val dictionaryCode = "american"

    val entryContent = EntryContent.fromXml(XML.loadString(rawResource("raw/entry/entryContentAmazing.xml")))
    Entry(entryId, entryId, Seq(), entryUrl, dictionaryCode, entryContent)
  }

}
