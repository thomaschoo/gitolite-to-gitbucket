package com.thomaschoo.helpers

import com.typesafe.config.ConfigFactory

import scala.collection.JavaConversions._

object Config {
  private val config = ConfigFactory.load()

  private val gitoliteConfig = config.getConfig("gitolite")
  private val gitoliteDbConfig = gitoliteConfig.getConfig("db")

  val gitoliteDbUrl = gitoliteDbConfig.getString("url")
  val gitoliteDbUser = gitoliteDbConfig.getString("user")
  val gitoliteDbPassword = gitoliteDbConfig.getString("password")

  val directory: String = gitoliteConfig.getString("directory")

  private val gitBucketConfig = config.getConfig("gitBucket")
  private val gitBucketDbConfig = gitBucketConfig.getConfig("db")

  val gitBucketDbUrl = gitBucketDbConfig.getString("url")
  val gitBucketDbUser = gitBucketDbConfig.getString("user")
  val gitBucketDbPassword = gitBucketDbConfig.getString("password")

  val keyTitle: String = gitBucketConfig.getString("keyTitle")

  val labelColours = gitBucketConfig.getObject("labelColours") map {
    case (key, conf) => key -> conf.render().replaceAll("\"", "")
  }

}
