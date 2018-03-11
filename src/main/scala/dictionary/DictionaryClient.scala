package dictionary

import client.{SkPublishAPI, SkPublishAPIException}

object DictionaryClient extends SkPublishAPI(ClientConfig.url, ClientConfig.apiKey) with ApiClient

private object ClientConfig {

  val url: String = scala.util.Properties
    .envOrNone("API_URL")
    .getOrElse(throw new IllegalArgumentException("No API URL"))

  val apiKey: String = scala.util.Properties
    .envOrNone("API_KEY")
    .getOrElse(throw new IllegalArgumentException("No API key"))

}

trait ApiClient {

  @throws(classOf[SkPublishAPIException])
  def getEntry(dictionaryCode: String, entryId: String, format: String): String

  @throws(classOf[SkPublishAPIException])
  def search(dictionaryCode: String, searchWord: String, pageSize: Integer, pageIndex: Integer): String

}