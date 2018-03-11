package repository.entry

import model.common.EntryContent
import org.scalatest.FlatSpec
import tools.RawResourceLoader

import scala.xml.XML

class EntryContentSpec extends FlatSpec with RawResourceLoader {

  private def parseResource(path: String): EntryContent = {
    val mockResponse = rawResource(path)
    val node = XML.loadString(mockResponse)
    EntryContent.fromXml(node)
  }

  "Entry" should "parse basic info" in {
    val content = parseResource("raw/entry/entryContentAmazing.xml")

    assert(content.id == "amazing")
    assert(content.partOfSpeech == "adjective")
    assert(content.stars == 2)
  }

  it should "parse pronunciation" in {
    val content = parseResource("raw/entry/entryContentAmazing.xml")

    val pronunciation = content.pronunciation
    assert(pronunciation.caption == "British English pronunciation: amazing")
    assert(pronunciation.pronunciation == "/əˈmeɪzɪŋ/")
    assert(pronunciation.region == "us")
    assert(pronunciation.audio.lengthCompare(2) == 0)
    assert(pronunciation.audio.head.`type` == "audio/mpeg")
    assert(pronunciation.audio.head.src == "https://www.macmillandictionary.com/media/american/us_pron/a/ama/amazi/amazing_American_English_pronunciation.mp3")
    assert(pronunciation.audio.apply(1).`type` == "audio/ogg")
    assert(pronunciation.audio.apply(1).src == "https://www.macmillandictionary.com/media/american/us_pron_ogg/a/ama/amazi/amazing_American_English_pronunciation.ogg")
  }

  it should "parse sense" in {
    val content = parseResource("raw/entry/entryContentAmazing.xml")
    val sense = content.sense

    val sense1 = sense.head
    assert(sense1.id == "amazing__1")
    assert(sense1.definition == "very surprising")
    assert(sense1.style.isEmpty)

    assert(sense1.examples.lengthCompare(2) == 0)
    assert(sense1.examples.head.id == "amazing__2")
    assert(sense1.examples.head.example == "Her story was quite amazing.")

    assert(sense1.meaning == "Making you feel surprised or amazed")
    assert(sense1.topic == "amazing__1")

    assert(sense1.synonyms.lengthCompare(10) == 0)
    assert(sense1.synonyms.head.entryId == "surprising")
    assert(sense1.synonyms.head.bookmark == "surprising__1")
    assert(sense1.synonyms.head.value == "surprising")

    assert(sense1.subsense.nonEmpty)
    assert(sense1.subsense.head.id == "amazing__4")
    assert(sense1.subsense.head.definition == "used about something surprising that is also very impressive")

    val sense2 = sense.apply(1)
    assert(sense2.id == "amazing__7")
    assert(sense2.style == "mainly spoken")
  }

  it should "parse additional content" in {
    val content = parseResource("raw/entry/entryContentAmazing.xml")
    val additional = content.additionalEntryContent.head

    assert(additional.base == "amazingly")
    assert(additional.id == "amazing__10")
    assert(additional.title == "derived word")
    assert(additional.pronunciation.pronunciation == "")
  }

}
