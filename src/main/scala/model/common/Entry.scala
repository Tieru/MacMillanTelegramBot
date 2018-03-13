package model.common

case class Entry
(
  entryId: String,
  entryLabel: String,
  topics: Seq[Topic],
  entryUrl: String,
  dictionaryCode: String,
  entryContent: EntryContent
)
