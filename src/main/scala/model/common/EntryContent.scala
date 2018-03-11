package model.common

import scala.xml.Node

case class EntryContent
(
  id: String,
  partOfSpeech: String,
  pronunciation: Pronunciation,
  stars: Int,
  sense: Seq[Sense],
  additionalEntryContent: Seq[AdditionalEntryContent]
)

object EntryContent {

  def fromXml(node: Node): EntryContent = {
    val id = node \@ "ID".trim
    val stars = (node \ "HEAD" \ "H_S1" \ "STARS").text.length
    val partOfSpeech = (node \ "HEAD" \ "H_S2" \ "PART-OF-SPEECH").text.trim

    val pronunciationNode = (node \ "HEAD" \ "H_S2" \ "PRONS").head
    val pronunciation = Pronunciation.fromXml(pronunciationNode)

    val senseNodes = node \ "SENSE"
    val sense: Seq[Sense] = for {
      senseNode <- senseNodes
    } yield Sense.fromXml(senseNode)

    val additionalEntryContent: Seq[AdditionalEntryContent] = for {
      additionalEntryNode <- node \ "RUNON"
    } yield AdditionalEntryContent.fromXml(additionalEntryNode)

    EntryContent(id, partOfSpeech, pronunciation, stars, sense, additionalEntryContent)
  }

}
