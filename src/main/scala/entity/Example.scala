package entity

import scala.xml.Node

case class Example(id: String, example: String)

object Example {

  def fromXml(node: Node): Example = {
    val id = (node \ "@ID").text
    val example = (node \ "EXAMPLE").text

    Example(id, example)
  }

}
