package mock

import model.WordEntry
import repository.source.EntryDataSource

class EntryDataSourceMock extends EntryDataSource {

  override def findWord(search: String): Option[WordEntry] = {

    search match {
      case "miracle" => Option(WordEntry(search))
      case _ => Option.empty
    }

  }
}
