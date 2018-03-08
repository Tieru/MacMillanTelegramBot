package dictionary

import scala.concurrent.{ExecutionContext, Future}


class ClientWrapper(api: ApiClient)(implicit ec: ExecutionContext) extends Api {

  def getEntry(entryId: String,
               dictionaryCode: String = Api.AMERICAN,
               format: String = Api.XML): Future[String] = Future[String] {
    api.getEntry(dictionaryCode, entryId, format)
  }

}

