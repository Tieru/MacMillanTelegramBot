package app

object Application extends App {

  override def main(args: Array[String]): Unit = {

    println("Starting bot...")

    DictionaryBot.run()
  }

}
