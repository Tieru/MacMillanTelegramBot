package response

import info.mukel.telegrambot4s.api.declarative.Messages
import info.mukel.telegrambot4s.methods.ParseMode.ParseMode
import info.mukel.telegrambot4s.models.{Message, ReplyMarkup}

class BotMessageContext(messages: Messages)(implicit message: Message) extends MessageContext {

  override def reply(text: String,
                     parseMode: Option[ParseMode],
                     disableWebPagePreview: Option[Boolean],
                     disableNotification: Option[Boolean],
                     replyToMessageId: Option[Int],
                     replyMarkup: Option[ReplyMarkup]): Unit = {

    messages.reply(text)
  }

}
