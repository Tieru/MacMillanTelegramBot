package model.common

import scala.xml.Node

case class EntryContent
(
  id: String,
  partOfSpeech: String,
  pronunciation: Option[Pronunciation],
  stars: Int,
  sense: Seq[Sense],
  additionalEntryContent: Seq[AdditionalEntryContent]
)

object EntryContent {

  def fromXml(node: Node): EntryContent = {
    val id = node \@ "ID".trim
    val stars = (node \ "HEAD" \ "H_S1" \ "STARS").text.length
    val partOfSpeech = (node \ "HEAD" \ "H_S2" \ "PART-OF-SPEECH").text.trim

    val pronunciationNode = (node \ "HEAD" \ "H_S2" \ "PRONS").headOption
    val pronunciation: Option[Pronunciation] = pronunciationNode match {
      case Some(value) => Option(Pronunciation.fromXml(value))
      case _ => None
    }

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
