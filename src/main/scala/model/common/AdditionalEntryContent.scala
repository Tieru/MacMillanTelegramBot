package model.common

import scala.xml.Node

case class AdditionalEntryContent
(
  id: String,
  title: String,
  base: String,
  pronunciation: Option[Pronunciation],
  partOfSpeech: String,
  examples: Seq[Example]
)

object AdditionalEntryContent {

  def fromXml(node: Node): AdditionalEntryContent = {
    val id = node \@ "ID".trim
    val title = (node \ "SEP").text.trim
    val base = (node \ "R-HEAD" \ "ENTRY" \ "BASE").text.trim

    val pronunciation: Option[Pronunciation] = (node \ "R-HEAD" \ "PRONS").headOption match {
      case Some(value) => Option(Pronunciation.fromXml(value))
      case _ => None
    }

    val partOfSpeech = (node \ "R-HEAD" \ "PART-OF-SPEECH").text.trim
    val examples: Seq[Example] = for {
      exampleNode <- node \ "EXAMPLES"
    } yield Example.fromXml(exampleNode)

    AdditionalEntryContent(id, title, base, pronunciation, partOfSpeech, examples)
  }

}
