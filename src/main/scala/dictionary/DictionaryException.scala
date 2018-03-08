package dictionary

import client.SkPublishAPIException

class DictionaryException(statusCode: Int = 0, response: String = "") extends SkPublishAPIException(statusCode, response)