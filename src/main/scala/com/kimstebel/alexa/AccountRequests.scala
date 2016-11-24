package com.kimstebel.alexa

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpRequest
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.ActorMaterializer
import com.kimstebel.alexa.AccountResponses.{Readings, Reading}
import de.heikoseeberger.akkahttpcirce.CirceSupport
import io.circe.generic.auto._

import scala.concurrent.{ExecutionContext, Future}

final case class AccountRequests(accountId: String)(
    implicit context: ExecutionContext,
    system: ActorSystem,
    materializer: ActorMaterializer
) extends CirceSupport {

  private[this] def accountUrl(relativePath: String) =
    s"http://meters.uat.ptl.ovotech.org.uk/accounts/$accountId$relativePath?source=DTMF"

  def fetchReadings(): Future[List[Readings]] =
    Http()
      .singleRequest(HttpRequest(uri = accountUrl("/readings")))
      .flatMap(response ⇒ Unmarshal(response).to[List[Readings]])

  def fetchLatestElectricityReading(): Future[Option[Reading]] =
    fetchReadings().map { readings ⇒
      readings
        .find(_.chargeItem == "Electricity")
        .flatMap(_.readings.find(_.actual))
    }
}
