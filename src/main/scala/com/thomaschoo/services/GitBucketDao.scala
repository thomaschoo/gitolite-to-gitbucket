package com.thomaschoo.services

import java.sql.Clob

import com.thomaschoo.helpers.Config
import com.thomaschoo.services.GitoliteDao.GitoliteProject

import models.gitbucket._
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
          case Some(_) => // Do nothing, duplicate
          case None =>
            Account.create(
              userName = user,
              mailAddress = gitoliteUser.email,
              password = "",
              administrator = gitoliteUser.admin,
              registeredDate = gitoliteUser.createdAt,
              updatedDate = gitoliteUser.updatedAt,
              groupAccount = false,
              fullName = name,
              removed = Option(gitoliteUser.blocked)
            )
        }
      }
    }
  }

  def insertRepositories(gitoliteProjects: List[GitoliteProject]): Unit = {
    def insertIssueIds(userName: String, repositoryName: String)(implicit session: DBSession): Unit = {
      IssueId.find(repositoryName, userName) match {
        case Some(_) => // Do nothing, duplicate
        case None => IssueId.create(userName, repositoryName, 0)
      }
    }

    NamedDB('gitBucket) localTx { implicit session =>
      gitoliteProjects foreach {
        case (gitoliteProject, gitoliteUser) =>
          val user = gitoliteUser.tid.getOrElse(throw new Exception)
          val repository = gitoliteProject.path.getOrElse(throw new Exception)

          Repository.find(repository, user) match {
            case Some(_) => // Do nothing, duplicate
            case None =>
              val descriptionClob: Option[Clob] = gitoliteProject.description match {
                case Some(description) =>
                  val clob: Clob = session.connection.createClob()
                  clob.setString(1, description)
                  Some(clob)
                case None => None
              }

              Repository.create(
                userName = user,
                repositoryName = repository,
                `private` = gitoliteProject.privateFlag,
                description = descriptionClob,
                defaultBranch = Some(gitoliteProject.defaultBranch),
                registeredDate = gitoliteProject.createdAt,
                updatedDate = gitoliteProject.updatedAt,
                lastActivityDate = gitoliteProject.updatedAt
              )
          }

          insertIssueIds(user, repository)
      }
    }
  }

  def insertCollaborators(gitoliteProject: GitoliteProject): Unit = {
    val (project, projectOwner) = gitoliteProject
    val users = GitoliteDao.getUsersForProject(project.id) filter {
      case (user, _) => user.id != projectOwner.id
    }

    NamedDB('gitBucket) localTx { implicit session =>
      users foreach {
        case (user, access) => access match {
          case ProjectAccess.Master =>
            val owner = projectOwner.tid.getOrElse(throw new Exception)
            val repository = project.path.getOrElse(throw new Exception)
            val collaborator = user.tid.getOrElse(throw new Exception)

            Collaborator.find(collaborator, repository, owner) match {
              case Some(_)  => // Do nothing, duplicate
              case None     => Collaborator.create(owner, repository, collaborator)
            }
          case ProjectAccess.Developer => insertRepositories((project, user) :: Nil)
          case ProjectAccess.Reporter => // Do nothing
        }
      }
    }
  }
}
