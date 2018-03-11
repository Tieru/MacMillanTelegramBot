package dictionary

import com.google.inject.Inject
import model.common.Dictionary
import model.common.Dictionary.Type

import scala.concurrent.{ExecutionContext, Future}


class ClientWrapper @Inject()(api: ApiClient)(implicit ec: ExecutionContext) extends Api {

  def getEntry(entryId: String,
               dictionaryCode: Type = Dictionary.American,
               format: String = Api.XML): Future[String] = Future[String] {
    api.getEntry(dictionaryCode.toString, entryId, format)
  }

}

