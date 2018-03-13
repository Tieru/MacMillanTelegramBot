package repository.entry.impl

import model.common.Entry

trait EntryCache {

  def store(entry: Entry)

  def get(entryId: String): Option[Entry]

}
