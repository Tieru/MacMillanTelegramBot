package dictionary

import client.{SkPublishAPI, SkPublishAPIException}

trait ApiClient {

  @throws(classOf[SkPublishAPIException])
  def getEntry(dictionaryCode: String, entryId: String, format: String): String
}

object DictionaryClient extends SkPublishAPI with ApiClient
