package dictionary

import com.google.inject.Inject
import model.common.Dictionary
import model.common.Dictionary.Type

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


class ClientWrapper @Inject()(api: ApiClient) extends Api {

  def getEntry(entryId: String,
               dictionaryCode: Type = Dictionary.American,
               format: String = Api.XML): Future[String] = Future[String] {
    api.getEntry(dictionaryCode.toString, entryId, format)
  }

}

