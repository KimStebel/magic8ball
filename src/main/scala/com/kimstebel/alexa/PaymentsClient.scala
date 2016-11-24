package com.kimstebel.alexa

object Payments {
  def makePayment(amount:Long) = {
    s"Are you sure you want to pay $amount pounds with the card ending in 4444?"
  }
  def confirmed = "Your payment has been taken. The transaction code is abcd1234."
}
