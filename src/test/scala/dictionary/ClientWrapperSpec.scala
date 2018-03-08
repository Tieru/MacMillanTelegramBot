package dictionary

import org.scalamock.scalatest.MockFactory
import org.scalatest.FlatSpec

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

class ClientWrapperSpec extends FlatSpec with MockFactory {

  private val client = mock[ApiClient]
  private val wrapper = new ClientWrapper(client)

  "Wrapper" should "return RawEntry object for 'entry' request" in {

    val expectedResult = "<>"
    (client.getEntry _).expects(Api.AMERICAN, * , Api.XML).returning(expectedResult)

    val result = Await.result(wrapper.getEntry(""), Duration.Inf)
    assert(result == expectedResult)
  }

}
