package com.thomaschoo.helpers

import com.typesafe.config.ConfigFactory

object Config {
  private val config = ConfigFactory.load()
  private val gitoliteConfig = config.getConfig("gitolite")
  private val gitBucketConfig = config.getConfig("gitBucket")

  val directory: String = gitoliteConfig.getString("directory")

  val keyTitle: String = gitBucketConfig.getString("keyTitle")
}
