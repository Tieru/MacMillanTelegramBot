package entity

import scala.xml.Node

case class PronunciationAudio(`type`: String, src: String)

object PronunciationAudio {

  def fromXml(node: Node): PronunciationAudio = {
    val `type` = node \@ "type"
    val src = node \@ "src"

    PronunciationAudio(`type`, src)
  }

}
