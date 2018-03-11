package dictionary

import model.common.Dictionary
import model.common.Dictionary.Type

import scala.concurrent.Future

trait Api {

  def getEntry(entryId: String,
               dictionaryCode: Type = Dictionary.American,
               format: String = Api.XML): Future[String]

  def search(search: String,
             offset: Int,
             count: Int = Api.defaultCount,
             dictionary: Type = Dictionary.American): Future[String]
}

object Api {

  val XML = "xml"
  val HTML = "html"

  val defaultCount = 10

}