package com.kimstebel.alexa

object AccountResponses {
  case class Readings(
      chargeItem: String,
      meterId: String,
      readings: List[Reading]
  )

  case class Reading(
      actual: Boolean,
      consumption: BigDecimal,
      date: String,
      schedule: String,
      `type`: String,
      validations: List[String],
      value: String
  )
}
