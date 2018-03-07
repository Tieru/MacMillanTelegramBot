import mock.EntryDataSourceMock
import model.{NothingFound, WordEntry}
import org.scalatest.FlatSpec
import repository.impl.EntryRepositoryImpl

class EntryRepositorySpec extends FlatSpec {

  private val dataSource = new EntryDataSourceMock()
  private val repository = new EntryRepositoryImpl(dataSource)

  "A Repository" should "return WordEntry when it finds a word" in {
    val desiredWord = "miracle"
    val result = repository.getEntry(desiredWord)
    assert(result.isInstanceOf[WordEntry])
    assert(result.asInstanceOf[WordEntry].word === desiredWord)
  }

  it should "return NothingFound when there is no words found" in {
    val result = repository.getEntry("whatever")
    assert(result.isInstanceOf[NothingFound])
  }

}
