package com.thomaschoo.services

import java.sql.Clob

import com.thomaschoo.helpers.Config

import models.gitbucket.{Account, SshKey}
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
            .leftJoin(SshKey as SshKey.sk).on(sqls"a.USER_NAME = sk.USER_NAME AND sk.TITLE = ${Config.keyTitle}")
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
