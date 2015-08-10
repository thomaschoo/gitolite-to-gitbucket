package com.thomaschoo.services

import java.sql.Clob

import models.gitbucket.{Account, SshKey}
import scalikejdbc._
import scalikejdbc.config._

object GitBucketDao {
  DBsWithEnv("gitbucket").setupAll()

  implicit val autoSession = AutoSession

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

    def createOrUpdateSshKeys(extendAccounts: List[(String, String, Option[SshKey])]): Unit = {
      DB localTx { implicit session =>
        extendAccounts foreach {
          case (username, filename, sshKey) =>
            val keyClob: Clob = session.connection.createClob()
            keyClob.setString(1, sshKeys(filename))

            sshKey match {
              case Some(k) =>
                SshKey(userName = k.userName, sshKeyId = k.sshKeyId, title = k.title, publicKey = keyClob).save()
              case None =>
                SshKey.create(username, title = "Origin Import", publicKey = keyClob)
            }
        }
      }
    }

    val extendedAccounts =
      withSQL {
        select
          .from(Account as Account.a)
          .leftJoin(SshKey as SshKey.sk).on(sqls"a.USER_NAME = sk.USER_NAME AND sk.TITLE = 'Origin Import'")
          .where(sqls"REPLACE(SUBSTRING(a.MAIL_ADDRESS, 0, LOCATE('@', a.MAIL_ADDRESS, -1) - 1), '.', '') IN (${sshKeys.keys})")
      }.map(extendAccount).list().apply()

    createOrUpdateSshKeys(extendedAccounts)
  }
}
