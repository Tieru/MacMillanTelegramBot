package dictionary

import scala.concurrent.Future

trait Api {

  def getEntry(entryId: String,
               dictionaryCode: String = Api.AMERICAN,
               format: String = Api.XML): Future[String]

}

object Api {

  val BRITISH = "british"
  val AMERICAN = "american"

  val XML = "xml"
  val HTML = "html"

}