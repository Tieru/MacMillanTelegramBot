package model.common

import scala.xml.Node

case class Example(id: String, example: String)

object Example {

  def fromXml(node: Node): Example = {
    val id = (node \ "@ID").text.trim
    val example = (node \ "EXAMPLE").text.trim

    Example(id, example)
  }

}
