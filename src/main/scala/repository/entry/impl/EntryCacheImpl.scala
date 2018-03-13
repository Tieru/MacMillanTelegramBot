package repository.entry.impl
import model.common.Entry

import scala.collection.mutable

class EntryCacheImpl extends EntryCache {

  private val cache = mutable.Map[String, Entry]()

  override def store(entry: Entry): Unit = {
    cache += (entry.entryId -> entry)
  }

  override def get(entryId: String): Option[Entry] = {
    cache.get(entryId)
  }
}
