package com.thomaschoo.services

import java.sql.Clob

import com.thomaschoo.helpers.Config
import com.thomaschoo.services.GitoliteDao.GitoliteProject

import models.gitbucket.{Account, Repository, SshKey}
import models.gitolite.Users
import scalikejdbc._

object GitBucketDao {
  DB.setup()

  GlobalSettings.loggingSQLAndTime = new LoggingSQLAndTimeSettings(
    enabled = true,
    singleLineMode = true,
    logLevel = 'DEBUG
  )

  def insertKeys(sshKeys: Map[String, String]): Unit = {
    def extendAccount(rs: WrappedResultSet): (String, String, Option[SshKey]) = {
      val username = rs.string("UN_on_a")
      val filename = rs.string("MA_on_a")

      val sshKey =
        try {
          Option(SshKey(SshKey.sk.resultName)(rs))
        } catch {
          case e: scalikejdbc.ResultSetExtractorException => Option.empty
        }

      (username, filename.substring(0, filename.indexOf("@")).replace(".", ""), sshKey)
    }

    def createOrUpdateSshKeys(extendAccounts: List[(String, String, Option[SshKey])])
                             (implicit session: DBSession): Unit = {
      extendAccounts foreach {
        case (username, filename, sshKey) =>
          val keyClob: Clob = session.connection.createClob()
          keyClob.setString(1, sshKeys(filename))

          sshKey match {
            case Some(k) =>
              SshKey(userName = k.userName, sshKeyId = k.sshKeyId, title = k.title, publicKey = keyClob).save()
            case None =>
              SshKey.create(username, title = Config.keyTitle, publicKey = keyClob)
          }
      }
    }

    NamedDB('gitBucket) localTx { implicit session =>
      val extendedAccounts =
        withSQL {
          select
            .from(Account as Account.a)
            .leftJoin(SshKey as SshKey.sk)
              .on(sqls.eq(Account.a.userName, SshKey.sk.userName).and.eq(SshKey.sk.title, Config.keyTitle))
            .where(sqls"REPLACE(SUBSTRING(a.MAIL_ADDRESS, 0, LOCATE('@', a.MAIL_ADDRESS, -1) - 1), '.', '') IN (${sshKeys.keys})")
        }.map(extendAccount).list().apply()

      createOrUpdateSshKeys(extendedAccounts)
    }
  }

  def insertAccounts(gitoliteUsers: List[Users]): Unit = {
    NamedDB('gitBucket) localTx { implicit session =>
      gitoliteUsers foreach { gitoliteUser =>
        val user = gitoliteUser.tid.getOrElse(throw new Exception) // TODO: Better exception
        val name = gitoliteUser.name.getOrElse(throw new Exception) // TODO: Better exception

        Account.find(user) match {
          case Some(_) =>
          case None =>
            Account.create(
              userName = user,
              mailAddress = gitoliteUser.email,
              password = "",
              administrator = gitoliteUser.admin,
              registeredDate = gitoliteUser.createdAt,
              updatedDate = gitoliteUser.updatedAt,
              groupAccount = false,
              fullName = name
            )
        }
      }
    }
  }

  def insertRepositories(gitoliteProjects: List[GitoliteProject]): Unit = {
    NamedDB('gitBucket) localTx { implicit session =>
      gitoliteProjects foreach {
        case (gitoliteProject, gitoliteUser) =>
          // TODO: Catch duplicate, check -> update or create
          val descriptionClob: Option[Clob] = gitoliteProject.description match {
            case Some(description) =>
              val clob: Clob = session.connection.createClob()
              clob.setString(1, description)
              Some(clob)
            case None => None
          }

          Repository.create(
            userName = gitoliteUser.tid.getOrElse(throw new Exception),
            repositoryName = gitoliteProject.name.getOrElse(throw new Exception),
            `private` = gitoliteProject.privateFlag,
            description = descriptionClob,
            defaultBranch = Some(gitoliteProject.defaultBranch),
            registeredDate = gitoliteProject.createdAt,
            updatedDate = gitoliteProject.updatedAt,
            lastActivityDate = gitoliteProject.updatedAt
          )
      }
    }
  }

}
