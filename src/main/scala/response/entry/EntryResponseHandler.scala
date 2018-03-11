package response.entry

import response.MessageContext

trait EntryResponseHandler {

  def handle(args: Seq[String])(implicit messageContext: MessageContext): Unit

}
