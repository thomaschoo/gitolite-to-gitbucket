package com.thomaschoo.services

import models.gitolite.Users
import scalikejdbc._

object GitoliteDao {
  DB.setup()

  GlobalSettings.loggingSQLAndTime = new LoggingSQLAndTimeSettings(
    enabled = true,
    singleLineMode = true,
    logLevel = 'DEBUG
  )

  def getUsers: List[Users] = {
    NamedDB('gitolite) readOnly { implicit session =>
      Users.findAll()
    }
  }

}
