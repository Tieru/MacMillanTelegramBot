package response.search

import model.common.Entry

import scala.concurrent.Future

trait EntriesSearchFetcher {

  def findEntries(query: String, offset: Int): Future[Seq[Entry]]

}
