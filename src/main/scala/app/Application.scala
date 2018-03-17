package app

import com.typesafe.scalalogging.Logger

object Application extends App {

  override def main(args: Array[String]): Unit = {

    val logger = Logger("Bot")

    logger.info("Starting bot...")

    DictionaryBot.run()
  }

}
