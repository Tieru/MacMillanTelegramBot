package repository.source

import model.WordEntry

trait EntryDataSource {

  def findWord(search: String): Option[WordEntry]

}
