package api

import java.net.URLEncoder
import javax.inject.Inject

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.Uri.{Authority, Host, Path, Query}
import akka.http.scaladsl.model._
import akka.http.scaladsl.model.headers.RawHeader
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.ActorMaterializer
import client.SkPublishAPIException
import com.typesafe.config.ConfigFactory
import model.common.Dictionary
import model.common.Dictionary.Type
import play.api.libs.json.Json
import repository.entry.RawEntry
import repository.search.SearchResults

import scala.concurrent.{ExecutionContext, Future}

class ApiClient @Inject()(implicit ec: ExecutionContext) extends Api {

  implicit val system: ActorSystem = ActorSystem("DictionaryApiClient")
  implicit val materializer: ActorMaterializer = ActorMaterializer()

  private val url = ConfigFactory.load().getString("api.url")
  private val apiKey: String = ConfigFactory.load().getString("api.key")

  def getEntry(entryId: String, dictionaryCode: Type = Dictionary.American): Future[RawEntry] = {

    val encodedEntryId = URLEncoder.encode(entryId, "UTF-8")
    val path = f"dictionaries/$dictionaryCode/entries/$encodedEntryId"
    val uri = defaultUri(path).withQuery(Query(("format", "xml")))

    request(HttpRequest(HttpMethods.GET).withUri(uri))
      .map(raw => Json.parse(raw).validate[RawEntry].get)
  }

  def search(search: String,
             offset: Int,
             count: Int = Api.defaultSearchCount,
             dictionaryCode: Type = Dictionary.American): Future[SearchResults] = {

    val encodedSearch = URLEncoder.encode(search, "UTF-8")
    val path = f"dictionaries/$dictionaryCode/search"
    val uri = defaultUri(path).withQuery(Query(("q", encodedSearch)))

    request(HttpRequest(HttpMethods.GET).withUri(uri))
      .map(raw => Json.parse(raw).validate[SearchResults].get)
  }

  def searchFirst(search: String, dictionaryCode: Type = Dictionary.American): Future[RawEntry] = {

    val encodedSearch = URLEncoder.encode(search, "UTF-8")
    val path = f"dictionaries/$dictionaryCode/search/first"
    val uri = defaultUri(path).withQuery(Query(("q", encodedSearch), ("format", "xml")))

    request(HttpRequest(HttpMethods.GET).withUri(uri))
      .map(raw => Json.parse(raw).validate[RawEntry].get)
  }

  private def request(data: HttpRequest): Future[String] = {
    val request = data.withHeaders(RawHeader("accessKey", apiKey))

    Http().singleRequest(request)
      .flatMap(response => {
        for {
          content <- Unmarshal(response).to[String]
        } yield {
          validateResponse(response, content)
          content
        }
      })
  }

  private def validateResponse(response: HttpResponse, responseContent: String): Unit = {
    val status = response.status.intValue()
    if (status != 200) {
      throw new SkPublishAPIException(status, responseContent)
    }
  }

  private def defaultUri(path: String): Uri = {
    Uri().withScheme("https").withAuthority(Authority(Host(url))).withPath(Path("/api/v1/" + path))
  }
}
