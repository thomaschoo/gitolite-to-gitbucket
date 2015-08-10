package com.thomaschoo

import java.sql.Clob

import models.gitbucket.{Account, SshKey}
import scalikejdbc._
import scalikejdbc.config._

class Migration() {
  DBsWithEnv("gitbucket").setupAll()

  implicit val autoSession = AutoSession

  GlobalSettings.loggingSQLAndTime = new LoggingSQLAndTimeSettings(
    enabled = true,
    singleLineMode = true,
    logLevel = 'DEBUG
  )

  def insertKey(key: String, filename: String): Unit = {
    def extendAccount(rs: WrappedResultSet): (String, Option[SshKey]) = {
      (rs.string("UN_on_a"),
        try {
          Option(SshKey(SshKey.sk.resultName)(rs))
        } catch {
          case e: scalikejdbc.ResultSetExtractorException => Option.empty
        }
      )
    }

    def createOrUpdateSshKey(userName: String, sshKey: Option[SshKey]): Unit = {
      DB readOnly { implicit session =>
        val keyClob: Clob = session.connection.createClob()
        keyClob.setString(1, key)

        sshKey match {
          case Some(k) =>
            SshKey(userName = k.userName, sshKeyId = k.sshKeyId, title = k.title, publicKey = keyClob).save()
          case None =>
            SshKey.create(userName, title = "Origin Import", publicKey = keyClob)
        }
      }
    }

    val extendedAccount =
      withSQL {
        select
          .from(Account as Account.a)
          .leftJoin(SshKey as SshKey.sk).on(sqls"a.USER_NAME = sk.USER_NAME AND sk.TITLE = 'Origin Import'")
          .where(sqls"REPLACE(SUBSTRING(a.MAIL_ADDRESS, 0, LOCATE('@', a.MAIL_ADDRESS, -1) - 1), '.', '') = $filename")
      }.map(extendAccount).single().apply()

    extendedAccount match {
      case Some((userName, sshKey)) => createOrUpdateSshKey(userName, sshKey)
      case None => //TODO: return useful message
    }
  }
}
