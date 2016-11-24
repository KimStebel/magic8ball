package com.kimstebel.alexa

import scala.util.Try
import org.scalactic.TripleEquals._

sealed trait Intent
case class MakePaymentAmount(amount: Int) extends Intent
case object Yes extends Intent
case object No extends Intent
case object Cancel extends Intent
case object LogIn extends Intent
case object ContactOvo extends Intent
case object EnergySaving extends Intent



object Intent {
  def from(intent: String, slots: Map[String, String]): Option[Intent] = {
    intent match {
      case "makePaymentAmount" => Try {
        MakePaymentAmount(slots.get("amount").get.toInt)
      }.toOption
      case "AMAZON.YesIntent" => Some(Yes)
      case "AMAZON.NoIntent" => Some(No)
      case "Amazon.CancelIntent" => Some(Cancel)
      case "logIn" => Some(LogIn)
      case "contactOvo" => Some(ContactOvo)
      case "energySaving" => Some(EnergySaving)
      case _ => None
    }
  }
}

sealed trait State extends Serializable
object State {
  def from(attributes: Map[String, String]): State = {
    attributes.get("state").map {
      case "PaymentToBeConfirmed" => PaymentToBeConfirmed
      case _ => Start
    }.getOrElse(Start)
  }
}
case object Start extends State
case object End extends State
case object PaymentToBeConfirmed extends State

case class Card(title: String, content: String)
case class Response(spoken: String, card: Option[Card] = None, reprompt: Option[String] = None)

object IntentHandler extends ((Option[Intent], State) => (Response, State)) {
  override def apply(intent: Option[Intent], state: State) = {
    intent match {
      case None => Response("What would you like to do? You can ask for you balance, your energy usage, or say 'help'.") -> End
      case Some(MakePaymentAmount(amount)) => {
        val answer = Payments.makePayment(amount)
        val newState = PaymentToBeConfirmed
        val reprompt = answer
        Response(answer, reprompt = Some(reprompt)) -> newState
      }
      case Some(Yes) if state === PaymentToBeConfirmed => {
        val answer = Payments.confirmed
        Response(answer) -> End
      }
      /* User has asked how to log in to MyOVO
      */
      case Some(LogIn) => Response("<speak>You can log in to your ohvo account at ovoenergy.com or give ohvo a call on <say-as interpret-as=\"spell-out\">01179<break strength=\"medium\" />303<break strength=\"medium\" /></say-as>100</speak>") -> state

      /* User has asked how to Contact OVO
      */
      case Some(ContactOvo) => Response("<speak>Go to ohvo energy dot com <break strength=\"medium\" /> and click My ohvo. Then, enter your email address and password. If you don't have a password, click<break strength=\"weak\" /> Forgotten your password<break strength=\"weak\" /> and you can set up a new one.</speak>") -> state

      /* User has asked how to save energy
      */
      case Some(EnergySaving) => Response("<speak> Did you know, you can save around £30 a year, just by remembering to turn your appliances off standby mode, or by using a bowl rather than running water when you wash up? <break strength=\"medium\" /> Check out the energy saving trust website <break strength=\"medium\" /> for more great tips on saving energy. </speak>") -> state


      case Some(intent) if Seq(No, Cancel).contains(intent) => {
        Response("Bye") -> End
      }
      case intent => {
        println(s"unhandled intent: ${intent}")
        Response("Sorry, something went wrong.") -> state
      }
    }
  }
}
