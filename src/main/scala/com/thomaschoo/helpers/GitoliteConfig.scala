package com.thomaschoo.helpers

import com.typesafe.config.ConfigFactory

object GitoliteConfig {
  private val config = ConfigFactory.load().getConfig("gitolite")

  val directory: String = config.getString("directory")
}
