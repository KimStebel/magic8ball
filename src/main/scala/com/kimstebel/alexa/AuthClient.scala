package com.kimstebel.alexa

import java.time.LocalTime
import org.http4s._
import org.http4s.dsl._
import org.http4s.client.blaze._
import org.json4s._
import org.json4s.native.JsonMethods._


/*
{
  "authenticated": true,
  "token": "M2I0MTg0ZGEtOTlhNy00M2FiLTlhNzgtMTg1NjRhZmFhYzMw",
  "persistentToken": "xwBNooItzXcsFCuXYiuMepwDHLZdkglnrLpVRIpb+V4=||100740355||yzWm9pyyktmorRPfwRa6xn9KlSaJhR7uhlqkpKL2UBc=",
  "userId": "57c95b04e4b08b78f109227c",
  "username": "100740355",
  "identifiers": {
    "global": "GT-CUS-100740355"
  },
  "roles": [
    "CUSTOMER"
  ],
  "userCreationTime": "2016-09-02T10:57:08.000Z"
}
*/

case class Identifiers(global: Option[String])

case class AuthResponse(
  token: String,
  persistentToken: String,
  userId: String,
  username: String,
  identifiers: Identifiers,
  roles: Seq[String],
  userCreationTime: LocalTime
)

object AuthClient {
  
  private val client = PooledHttp1Client()
  
  def apply = {
    val Seq(user, password, account) = Seq("user", "password", "account").map(key => Config.conf.getString(s"""secrets."alexa.uat.test-$key""""))
    
    val loginUri = Uri.uri("https://myovo-uat.ovoenergy.com/api/auth/login")
    val req = Request(POST, loginUri).withBody(s"""
{
    "username": "$user",
    "password": "$password",
    "rememberMe": true
}
""").replaceAllHeaders(Header("Accept", "application/json")).withType(MediaType.`application/json`)

//replaceAllHeaders(Header("Content-Type", "application/json;charset=UTF-8"))
    val res = client.expect[String](req).run
    implicit val formats = DefaultFormats
    
    
    
    (user, password, account)
  }
}