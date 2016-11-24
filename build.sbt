enablePlugins(AwsLambdaPlugin)

lazy val root = (project in file(".")).
  settings(
    name := "magic8ball",
    version := "0.0.1",
    scalaVersion := "2.11.8"
  ).
  settings(
    libraryDependencies ++= dependencies
  )
  
  
lazy val http4sVersion = "0.14.11"

val circeVersion = "0.6.1"

lazy val dependencies = Seq(
    "com.amazon.alexa" % "alexa-skills-kit" % "1.1.3",
    "com.fasterxml.jackson.module" % "jackson-module-scala_2.11" % "2.7.5",
    "org.apache.logging.log4j" % "log4j-core" % "2.6.2",
    "org.slf4j" % "slf4j-api" % "1.7.21",
    "org.apache.commons" % "commons-lang3" % "3.4",
    "commons-io" % "commons-io" % "2.5",
    "com.amazonaws" % "aws-lambda-java-core" % "1.1.0",
    "com.amazonaws" % "aws-java-sdk-dynamodb" % "1.11.31",
  "org.http4s" %% "http4s-dsl" % http4sVersion,
  "org.http4s" %% "http4s-blaze-server" % http4sVersion,
  "org.http4s" %% "http4s-blaze-client" % http4sVersion,
  "org.scalactic" %% "scalactic" % "3.0.0",
  "org.scalatest" %% "scalatest" % "3.0.0" % "test",
  "com.amazon.alexa" % "alexa-skills-kit" % "1.1.3",
  "org.apache.logging.log4j" % "log4j-core" % "2.6.2",
  "org.slf4j" % "slf4j-api" % "1.7.21",
  "com.amazonaws" % "aws-lambda-java-core" % "1.1.0",
  "com.typesafe" % "config" % "1.3.1",
  "org.json4s" %% "json4s-native" % "3.5.0",
  "com.typesafe.akka" %% "akka-http" % "10.0.0",
  "de.heikoseeberger" %% "akka-http-circe" % "1.11.0",
  "io.circe" %% "circe-core" % circeVersion,
  "io.circe" %% "circe-generic" % circeVersion,
  "io.circe" %% "circe-parser" % circeVersion
)

handlerName := Some("com.freqlabs.magic8ball.Magic8BallSpeechletRequestStreamHandler")
region := Some("us-east-1")
