package com.thomaschoo.services

import models.gitolite.{Projects, Users, UsersProjects}
import scalikejdbc._

object GitoliteDao {
  DB.setup()

  GlobalSettings.loggingSQLAndTime = new LoggingSQLAndTimeSettings(
    enabled = true,
    singleLineMode = true,
    logLevel = 'DEBUG
  )

  type GitoliteProject = (Projects, Users)
  type ProjectAccess = Int

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

  def getUsersForProject(projectId: Int): List[(Users, ProjectAccess)] = {
    // TODO: project access mapping (40 = master, 20 = Reporter, 30 = Developer)
    NamedDB('gitolite) readOnly { implicit session =>
      withSQL {
        select
          .from(UsersProjects as UsersProjects.up)
          .leftJoin(Users as Users.u).on(UsersProjects.up.userId, Users.u.id)
          .where.eq(UsersProjects.up.projectId, projectId)
      }.map(rs => (Users(Users.u)(rs), rs.int("pa_on_up"))).list().apply()
    }
  }

}
