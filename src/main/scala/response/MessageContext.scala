package response

import info.mukel.telegrambot4s.methods.ParseMode.ParseMode
import info.mukel.telegrambot4s.models.ReplyMarkup

trait MessageContext {

  def reply(text: String,
            parseMode: Option[ParseMode] = None,
            disableWebPagePreview: Option[Boolean] = None,
            disableNotification: Option[Boolean] = None,
            replyToMessageId: Option[Int] = None,
            replyMarkup: Option[ReplyMarkup] = None
           ): Unit

}
