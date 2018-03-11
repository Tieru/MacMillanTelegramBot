package model.common

import scala.xml.Node

case class Sense
(
  id: String,
  definition: String,
  style: String,
  examples: Seq[Example],
  meaning: String,
  topic: String,
  synonyms: Seq[Synonym],
  subsense: Seq[Sense]
)

object Sense {

  def fromXml(node: Node, isSubsense: Boolean = false): Sense = {
    val id = node \@ "ID"
    val senseContentNode = if (isSubsense) {
      node \ "SUB-SENSE-CONTENT"
    } else {
      node \ "SENSE-CONTENT"
    }

    val definition = (senseContentNode \ "DEFINITION").text.trim
    val style = (senseContentNode \ "STYLE-LEVEL").text.trim

    val examples: Seq[Example] = for {
      exampleNode <- senseContentNode \ "EXAMPLES"
    } yield Example.fromXml(exampleNode)

    val meaning = (senseContentNode \ "TXREF" \@ "txtitle").trim.replace(":", "")
    val topic = senseContentNode \ "TXREF" \@ "topic"

    val synonyms: Seq[Synonym] = for {
      synonymNode <- senseContentNode \ "TXREF" \ "Ref"
    } yield Synonym.fromXml(synonymNode)

    val subsenses: Seq[Sense] = for {
      subsenseNode <- senseContentNode \ "SUB-SENSE"
    } yield Sense.fromXml(subsenseNode, isSubsense = true)

    Sense(id, definition, style, examples, meaning, topic, synonyms, subsenses)
  }

}
