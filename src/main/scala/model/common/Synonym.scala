package model.common

import scala.xml.Node

case class Synonym(entryId: String, bookmark: String, value: String)

object Synonym {

  def fromXml(node: Node): Synonym = {
    val entryId = (node \ "@entryId").text
    val bookmark = (node \ "@bookmark").text
    val value = node.text

    Synonym(entryId, bookmark, value)
  }

}
