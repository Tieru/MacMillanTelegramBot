package response.search

import com.google.inject.Inject
import model.common.Entry
import repository.entry.EntryRepository
import repository.search.{SearchRepository, SearchResults}

import scala.concurrent.{ExecutionContext, Future}


class EntriesSearchResultsFetcherImpl @Inject()(searchRepository: SearchRepository, entryRepository: EntryRepository)(implicit ec: ExecutionContext) extends EntriesSearchFetcher {

  def findEntries(query: String, offset: Int): Future[Seq[Entry]] = {
    searchRepository.search(query, offset).flatMap(mapSearchResults)
  }

  private def mapSearchResults(searchResults: Option[SearchResults]): Future[Seq[Entry]] = {
    val results = searchResults.getOrElse {
      return Future.successful(Seq())
    }.results


    val entries: Seq[Future[Option[Entry]]] = for {
      result <- results
    } yield entryRepository.getEntry(result.entryId)

    Future.sequence(entries).map(_.flatten)
  }
}
