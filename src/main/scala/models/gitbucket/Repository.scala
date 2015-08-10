package models.gitbucket

import java.sql.Clob

import org.joda.time.DateTime

import scalikejdbc._

case class Repository(
  userName: String,
  repositoryName: String,
  `private`: Boolean,
  description: Option[Clob] = None,
  defaultBranch: Option[String] = None,
  registeredDate: DateTime,
  updatedDate: DateTime,
  lastActivityDate: DateTime,
  originUserName: Option[String] = None,
  originRepositoryName: Option[String] = None,
  parentUserName: Option[String] = None,
  parentRepositoryName: Option[String] = None) {

  def save()(implicit session: DBSession = Repository.autoSession): Repository = Repository.save(this)(session)

  def destroy()(implicit session: DBSession = Repository.autoSession): Unit = Repository.destroy(this)(session)

}


object Repository extends SQLSyntaxSupport[Repository] {

  override val tableName = "REPOSITORY"

  override val columns = Seq("USER_NAME", "REPOSITORY_NAME", "PRIVATE", "DESCRIPTION", "DEFAULT_BRANCH", "REGISTERED_DATE", "UPDATED_DATE", "LAST_ACTIVITY_DATE", "ORIGIN_USER_NAME", "ORIGIN_REPOSITORY_NAME", "PARENT_USER_NAME", "PARENT_REPOSITORY_NAME")

  def apply(r: SyntaxProvider[Repository])(rs: WrappedResultSet): Repository = apply(r.resultName)(rs)
  def apply(r: ResultName[Repository])(rs: WrappedResultSet): Repository = new Repository(
    userName = rs.get(r.userName),
    repositoryName = rs.get(r.repositoryName),
    `private` = rs.get(r.`private`),
    description = rs.get(r.description),
    defaultBranch = rs.get(r.defaultBranch),
    registeredDate = rs.get(r.registeredDate),
    updatedDate = rs.get(r.updatedDate),
    lastActivityDate = rs.get(r.lastActivityDate),
    originUserName = rs.get(r.originUserName),
    originRepositoryName = rs.get(r.originRepositoryName),
    parentUserName = rs.get(r.parentUserName),
    parentRepositoryName = rs.get(r.parentRepositoryName)
  )

  val r = Repository.syntax("r")

  override val autoSession = AutoSession

  def find(repositoryName: String, userName: String)(implicit session: DBSession = autoSession): Option[Repository] = {
    withSQL {
      select.from(Repository as r).where.eq(r.repositoryName, repositoryName).and.eq(r.userName, userName)
    }.map(Repository(r.resultName)).single.apply()
  }

  def findAll()(implicit session: DBSession = autoSession): List[Repository] = {
    withSQL(select.from(Repository as r)).map(Repository(r.resultName)).list.apply()
  }

  def countAll()(implicit session: DBSession = autoSession): Long = {
    withSQL(select(sqls.count).from(Repository as r)).map(rs => rs.long(1)).single.apply().get
  }

  def findBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Option[Repository] = {
    withSQL {
      select.from(Repository as r).where.append(where)
    }.map(Repository(r.resultName)).single.apply()
  }

  def findAllBy(where: SQLSyntax)(implicit session: DBSession = autoSession): List[Repository] = {
    withSQL {
      select.from(Repository as r).where.append(where)
    }.map(Repository(r.resultName)).list.apply()
  }

  def countBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Long = {
    withSQL {
      select(sqls.count).from(Repository as r).where.append(where)
    }.map(_.long(1)).single.apply().get
  }

  def create(
    userName: String,
    repositoryName: String,
    `private`: Boolean,
    description: Option[Clob] = None,
    defaultBranch: Option[String] = None,
    registeredDate: DateTime,
    updatedDate: DateTime,
    lastActivityDate: DateTime,
    originUserName: Option[String] = None,
    originRepositoryName: Option[String] = None,
    parentUserName: Option[String] = None,
    parentRepositoryName: Option[String] = None)(implicit session: DBSession = autoSession): Repository = {
    withSQL {
      insert.into(Repository).columns(
        column.userName,
        column.repositoryName,
        column.`private`,
        column.description,
        column.defaultBranch,
        column.registeredDate,
        column.updatedDate,
        column.lastActivityDate,
        column.originUserName,
        column.originRepositoryName,
        column.parentUserName,
        column.parentRepositoryName
      ).values(
        userName,
        repositoryName,
        `private`,
        description,
        defaultBranch,
        registeredDate,
        updatedDate,
        lastActivityDate,
        originUserName,
        originRepositoryName,
        parentUserName,
        parentRepositoryName
      )
    }.update.apply()

    Repository(
      userName = userName,
      repositoryName = repositoryName,
      `private` = `private`,
      description = description,
      defaultBranch = defaultBranch,
      registeredDate = registeredDate,
      updatedDate = updatedDate,
      lastActivityDate = lastActivityDate,
      originUserName = originUserName,
      originRepositoryName = originRepositoryName,
      parentUserName = parentUserName,
      parentRepositoryName = parentRepositoryName)
  }

  def save(entity: Repository)(implicit session: DBSession = autoSession): Repository = {
    withSQL {
      update(Repository).set(
        column.userName -> entity.userName,
        column.repositoryName -> entity.repositoryName,
        column.`private` -> entity.`private`,
        column.description -> entity.description,
        column.defaultBranch -> entity.defaultBranch,
        column.registeredDate -> entity.registeredDate,
        column.updatedDate -> entity.updatedDate,
        column.lastActivityDate -> entity.lastActivityDate,
        column.originUserName -> entity.originUserName,
        column.originRepositoryName -> entity.originRepositoryName,
        column.parentUserName -> entity.parentUserName,
        column.parentRepositoryName -> entity.parentRepositoryName
      ).where.eq(column.repositoryName, entity.repositoryName).and.eq(column.userName, entity.userName)
    }.update.apply()
    entity
  }

  def destroy(entity: Repository)(implicit session: DBSession = autoSession): Unit = {
    withSQL { delete.from(Repository).where.eq(column.repositoryName, entity.repositoryName).and.eq(column.userName, entity.userName) }.update.apply()
  }

}
