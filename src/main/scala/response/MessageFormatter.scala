package response

import model.common.{AdditionalEntryContent, Entry, Sense}

object MessageFormatter {

  def formatEntry(entry: Entry): String = {
    val content = entry.entryContent
    val word = entry.entryLabel.capitalize

    val pronunciation = content.pronunciation match {
      case Some(value) => value.pronunciation
      case _ => ""
    }

    val partOfSpeech = content.partOfSpeech.capitalize

    var text = word
    if (pronunciation.nonEmpty) {
      text += f" - $pronunciation"
    }

    if (partOfSpeech.nonEmpty) {
      text += f" - $partOfSpeech"
    }

    text += "\n\n"

    for ((sense, i) <- content.sense.zipWithIndex) {
      val senseText = buildSenseText(sense, (i + 1).toString)
      text += senseText + '\n'
    }

    for (additional <- content.additionalEntryContent) {
      text += buildAdditionalContent(additional)
    }

    text.trim
  }

  private def buildSenseText(sense: Sense, index: String): String = {
    var text = index + ". "

    val style = sense.style.capitalize
    if (style.nonEmpty) {
      text += f"($style) "
    }

    text += sense.definition.capitalize + '\n'

    for (example <- sense.examples) {
      val value = example.example
      text += f"  - $value\n"
    }

    if (sense.meaning.nonEmpty || sense.synonyms.nonEmpty) {
      text += '\n'
    }

    val meaning = sense.meaning
    if (meaning.nonEmpty) {
      text += f"Related: $meaning\n"
    }

    if (sense.synonyms.nonEmpty) {
      text += "Synonyms: "

      for (synonym <- sense.synonyms) {
        text += synonym.value + ", "
      }
      text = text.dropRight(2)
      text += '\n'
    }

    for ((subsense, i) <- sense.subsense.zipWithIndex) {
      text += '\n' + buildSenseText(subsense, index + "." + (i + 1))
    }

    text
  }

  private def buildAdditionalContent(content: AdditionalEntryContent): String = {
    var text = content.title.capitalize + '\n' + content.base.capitalize

    val additionaPartOfSpeech = content.partOfSpeech.capitalize
    if (additionaPartOfSpeech.nonEmpty) {
      text += f" - $additionaPartOfSpeech"
    }

    text += '\n'

    for (example <- content.examples) {
      val value = example.example
      text += f"  - $value\n"
    }

    text
  }

}
