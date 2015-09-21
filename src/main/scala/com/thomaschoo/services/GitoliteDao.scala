package com.thomaschoo.services

import models.gitolite.{UsersProjects, Projects, Users}
import scalikejdbc._

object GitoliteDao {
  DB.setup()

  GlobalSettings.loggingSQLAndTime = new LoggingSQLAndTimeSettings(
    enabled = true,
    singleLineMode = true,
    logLevel = 'DEBUG
  )

  type GitoliteProject = (Projects, Users)

  def getUsers: List[Users] = {
    NamedDB('gitolite) readOnly { implicit session =>
      Users.findAll()
    }
  }

  def getProjects: List[GitoliteProject] = {
    NamedDB('gitolite) readOnly { implicit session =>
      withSQL {
        select
          .from(Projects as Projects.p)
          .leftJoin(Users as Users.u).on(Projects.p.ownerId, Users.u.id)
      }.map(rs => (Projects(Projects.p)(rs), Users(Users.u)(rs))).list().apply()
    }
  }

  }

}
