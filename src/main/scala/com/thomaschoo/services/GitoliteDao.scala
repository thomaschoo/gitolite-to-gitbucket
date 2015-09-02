package com.thomaschoo.services

import models.gitolite.Users
import scalikejdbc._
import scalikejdbc.config._

object GitoliteDao {
  implicit val autoSession = AutoSession

  GlobalSettings.loggingSQLAndTime = new LoggingSQLAndTimeSettings(
    enabled = true,
    singleLineMode = true,
    logLevel = 'DEBUG
  )

  def getUsers: List[Users] = {
    DBsWithEnv("gitolite").setupAll()

    Users.findAll()
  }

}
