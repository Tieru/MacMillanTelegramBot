package repository.entry

import org.scalatest.FlatSpec
import repository.entry.impl.EntryCacheImpl
import tools.RawResourceLoader

class EntryCacheSpec extends FlatSpec with RawResourceLoader {

  val cache = new EntryCacheImpl()

  "Cache" should "return None for non cached entries" in {

    val result = cache.get("non cached entry")
    assert(result.isEmpty)
  }

  it should "accept and return cached values" in {

    val entryId = "amazing"

    val entry = TestEntities.createEntry(entryId)
    cache.store(entry)

    val result = cache.get(entryId).get
    assert(result === entry)
  }

}
