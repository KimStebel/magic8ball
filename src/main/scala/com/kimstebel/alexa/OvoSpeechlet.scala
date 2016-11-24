package com.kimstebel.alexa

import java.util.HashSet

import org.scalactic.TripleEquals._

import com.amazon.speech.speechlet.{ IntentRequest, LaunchRequest, Session, SessionEndedRequest, SessionStartedRequest, Speechlet, SpeechletRequest, SpeechletResponse }
import com.amazon.speech.speechlet.lambda.SpeechletRequestStreamHandler
import com.amazon.speech.ui.{ Card => AlexaCard, OutputSpeech, PlainTextOutputSpeech, Reprompt, SimpleCard }

import scala.collection.JavaConverters._

class OvoSpeechletRequestStreamHandler extends SpeechletRequestStreamHandler(new OvoSpeechlet(IntentHandler), OvoSpeechlet.appIds)

class OvoSpeechlet(handleIntent:(Option[Intent], State) => (Response, State)) extends Speechlet {
  import OvoSpeechlet._

  override def onSessionStarted(request: SessionStartedRequest, session: Session) = {
    logInvocation("onSessionStarted", request, session)
  }

  override def onLaunch(request: LaunchRequest, session: Session): SpeechletResponse = {
    logInvocation("onLaunch", request, session)
    val (response, newState) = handleIntent(None, Start)
    speechletResponse(response, newState)
  }

  override def onIntent(request: IntentRequest, session: Session): SpeechletResponse = {
    logInvocation("onIntent", request, session)
    val intent = Intent.from(request.getIntent.getName, request.getIntent.getSlots.asScala.toMap.mapValues(_.getValue))
    val state = State.from(session.getAttributes.asScala.toMap.mapValues{case s:String => s})
    (speechletResponse _).tupled(IntentHandler(intent, state))
  }

  override def onSessionEnded(request: SessionEndedRequest, session: Session) = {
    logInvocation("onSessionEnded", request, session)
  }
}

object OvoSpeechlet {
  
  val appIds = {
    val res = new HashSet[String]
    res.add("amzn1.ask.skill.bdde335d-c183-4e1c-a5f3-ad7d62f984d1")
    res
  }
  
  private def speechletResponse(response: Response, newState: State) = {
    println(s"response: $response, new state: $newState")
    new SpeechletResponse {
      setOutputSpeech(outputSpeech(response.spoken))
      setCard(response.card.map(card => {
        val result = new SimpleCard
        result.setTitle(card.title)
        result.setContent(card.content)
        result
      }).getOrElse(null))
      setReprompt(response.reprompt.map(reprompt => {
        new Reprompt {
          setOutputSpeech(outputSpeech(reprompt))
        }
      }).getOrElse(null))
      setShouldEndSession(newState === End)
    }
  }
  
  private def outputSpeech(text:String) = new PlainTextOutputSpeech {
    setText(text)
  }
  
  private def logInvocation(name: String, request: SpeechletRequest, session: Session) = {
    println(s"$name requestId=${request.getRequestId} sessionId=${session.getSessionId}")
  }
}
