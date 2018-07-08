package model.common

case class Entry
(
  entryId: String,
  entryLabel: String,
  topics: Seq[Topic],
  dictionaryCode: String,
  entryContent: EntryContent
)
