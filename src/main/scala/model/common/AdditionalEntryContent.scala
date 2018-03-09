package model.common

import scala.xml.Node

case class AdditionalEntryContent
(
  id: String,
  title: String,
  base: String,
  pronunciation: Pronunciation,
  partOfSpeech: String,
  examples: Seq[Example]
)

object AdditionalEntryContent {

  def fromXml(node: Node): AdditionalEntryContent = {
    val id = node \@ "ID"
    val title = (node \ "SEP").text
    val base = (node \ "R-HEAD" \ "ENTRY" \ "BASE").text
    val pronunciation = Pronunciation.fromXml((node \ "R-HEAD" \ "PRONS").head)
    val partOfSpeech = (node \ "R-HEAD" \ "PART-OF-SPEECH").text
    val examples: Seq[Example] = for {
      exampleNode <- node \ "EXAMPLES"
    } yield Example.fromXml(exampleNode)

    AdditionalEntryContent(id, title, base, pronunciation, partOfSpeech, examples)
  }

}
