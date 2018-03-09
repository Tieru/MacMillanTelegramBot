package repository.entry.model

import model.common.Topic

case class Entry
(
  entryId: String,
  entryLabel: String,
  topics: Array[Topic],
  entryUrl: String,
  dictionaryCode: String,
  entryContent: EntryContent
)
