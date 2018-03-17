package app

import com.typesafe.scalalogging.Logger

object Application extends App {

  private val logger = Logger("Bot")

  override def main(args: Array[String]): Unit = {

    logger.info("Starting bot...")

    DictionaryBot.run()
  }

}
