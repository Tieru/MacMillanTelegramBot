package entity

import scala.xml.Node

case class Pronunciation(pronunciation: String, audio: Seq[PronunciationAudio], region: String, caption: String)

object Pronunciation {

  def fromXml(node: Node): Pronunciation = {
    val pronunciation = (node \ "PRON").text.replace("/", "")

    val audio = node \ "audio"
    val region = audio \@ "region"
    val caption = audio \@ "caption"

    val pronunciationAudios: Seq[PronunciationAudio] = for {
      record <- audio \ "source"
    } yield PronunciationAudio.fromXml(record)

    Pronunciation(pronunciation, pronunciationAudios, region, caption)
  }

}
