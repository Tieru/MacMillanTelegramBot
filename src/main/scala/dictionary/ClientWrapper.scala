package dictionary

import com.google.inject.Inject
import com.typesafe.scalalogging.Logger
import model.common.Dictionary
import model.common.Dictionary.Type

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


class ClientWrapper @Inject()(api: ApiClient) extends Api {

  private val logger = Logger(classOf[ClientWrapper])

  def getEntry(entryId: String,
               dictionaryCode: Type = Dictionary.American,
               format: String = Api.XML): Future[String] = Future[String] {
    api.getEntry(dictionaryCode.toString, entryId, format)
  }

  def searchFirst(search: String, dictionaryCode: Type = Dictionary.American,
                  format: String = Api.XML): Future[String] = Future[String] {
    api.searchFirst(dictionaryCode.toString, search, format)
  }

  def search(search: String,
             offset: Int,
             count: Int = Api.defaultCount,
             dictionary: Type = Dictionary.American): Future[String] = Future[String] {
    val dictionaryValue = dictionary.toString
    val pageIndex = (offset / count) + 1
    logger.debug(f"Executing request 'search' with search query '$search', page index $pageIndex, count $count, dictionary '$dictionary'")
    api.search(dictionaryValue, search, count, pageIndex)
  }

}

