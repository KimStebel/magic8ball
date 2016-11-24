package com.kimstebel.alexa

import com.typesafe.config.ConfigFactory
import com.typesafe.config.ConfigParseOptions
import com.typesafe.config.ConfigSyntax
import com.typesafe.config.ConfigResolveOptions

object Config {
  val conf = {
    val main = ConfigFactory.load("application.conf")
    val secrets = ConfigFactory.load("secrets.json")
    main.withFallback(secrets.atPath("secrets"))
  }
  
  
}