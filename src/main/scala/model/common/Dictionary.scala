package model.common

import model.common

object Dictionary extends Enumeration {

  type Type = Value
  val American: common.Dictionary.Value = Value("american")
  val British: common.Dictionary.Value = Value("british")

}
