package models.gitbucket

import java.sql.Clob

import org.joda.time.DateTime

import scalikejdbc._

case class CommitStatus(
  commitStatusId: Int,
  userName: String,
  repositoryName: String,
  commitId: String,
  context: String,
  state: String,
  targetUrl: Option[String] = None,
  description: Option[Clob] = None,
  creator: String,
  registeredDate: DateTime,
  updatedDate: DateTime) {

  def save()(implicit session: DBSession = CommitStatus.autoSession): CommitStatus = CommitStatus.save(this)(session)

  def destroy()(implicit session: DBSession = CommitStatus.autoSession): Unit = CommitStatus.destroy(this)(session)

}


object CommitStatus extends SQLSyntaxSupport[CommitStatus] {

  override val tableName = "COMMIT_STATUS"

  override val columns = Seq("COMMIT_STATUS_ID", "USER_NAME", "REPOSITORY_NAME", "COMMIT_ID", "CONTEXT", "STATE", "TARGET_URL", "DESCRIPTION", "CREATOR", "REGISTERED_DATE", "UPDATED_DATE")

  def apply(cs: SyntaxProvider[CommitStatus])(rs: WrappedResultSet): CommitStatus = apply(cs.resultName)(rs)
  def apply(cs: ResultName[CommitStatus])(rs: WrappedResultSet): CommitStatus = new CommitStatus(
    commitStatusId = rs.get(cs.commitStatusId),
    userName = rs.get(cs.userName),
    repositoryName = rs.get(cs.repositoryName),
    commitId = rs.get(cs.commitId),
    context = rs.get(cs.context),
    state = rs.get(cs.state),
    targetUrl = rs.get(cs.targetUrl),
    description = rs.get(cs.description),
    creator = rs.get(cs.creator),
    registeredDate = rs.get(cs.registeredDate),
    updatedDate = rs.get(cs.updatedDate)
  )

  val cs = CommitStatus.syntax("cs")

  override val autoSession = AutoSession

  def find(commitStatusId: Int)(implicit session: DBSession = autoSession): Option[CommitStatus] = {
    withSQL {
      select.from(CommitStatus as cs).where.eq(cs.commitStatusId, commitStatusId)
    }.map(CommitStatus(cs.resultName)).single.apply()
  }

  def findAll()(implicit session: DBSession = autoSession): List[CommitStatus] = {
    withSQL(select.from(CommitStatus as cs)).map(CommitStatus(cs.resultName)).list.apply()
  }

  def countAll()(implicit session: DBSession = autoSession): Long = {
    withSQL(select(sqls.count).from(CommitStatus as cs)).map(rs => rs.long(1)).single.apply().get
  }

  def findBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Option[CommitStatus] = {
    withSQL {
      select.from(CommitStatus as cs).where.append(where)
    }.map(CommitStatus(cs.resultName)).single.apply()
  }

  def findAllBy(where: SQLSyntax)(implicit session: DBSession = autoSession): List[CommitStatus] = {
    withSQL {
      select.from(CommitStatus as cs).where.append(where)
    }.map(CommitStatus(cs.resultName)).list.apply()
  }

  def countBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Long = {
    withSQL {
      select(sqls.count).from(CommitStatus as cs).where.append(where)
    }.map(_.long(1)).single.apply().get
  }

  def create(
    userName: String,
    repositoryName: String,
    commitId: String,
    context: String,
    state: String,
    targetUrl: Option[String] = None,
    description: Option[Clob] = None,
    creator: String,
    registeredDate: DateTime,
    updatedDate: DateTime)(implicit session: DBSession = autoSession): CommitStatus = {
    val generatedKey = withSQL {
      insert.into(CommitStatus).columns(
        column.userName,
        column.repositoryName,
        column.commitId,
        column.context,
        column.state,
        column.targetUrl,
        column.description,
        column.creator,
        column.registeredDate,
        column.updatedDate
      ).values(
        userName,
        repositoryName,
        commitId,
        context,
        state,
        targetUrl,
        description,
        creator,
        registeredDate,
        updatedDate
      )
    }.updateAndReturnGeneratedKey.apply()

    CommitStatus(
      commitStatusId = generatedKey.toInt,
      userName = userName,
      repositoryName = repositoryName,
      commitId = commitId,
      context = context,
      state = state,
      targetUrl = targetUrl,
      description = description,
      creator = creator,
      registeredDate = registeredDate,
      updatedDate = updatedDate)
  }

  def save(entity: CommitStatus)(implicit session: DBSession = autoSession): CommitStatus = {
    withSQL {
      update(CommitStatus).set(
        column.commitStatusId -> entity.commitStatusId,
        column.userName -> entity.userName,
        column.repositoryName -> entity.repositoryName,
        column.commitId -> entity.commitId,
        column.context -> entity.context,
        column.state -> entity.state,
        column.targetUrl -> entity.targetUrl,
        column.description -> entity.description,
        column.creator -> entity.creator,
        column.registeredDate -> entity.registeredDate,
        column.updatedDate -> entity.updatedDate
      ).where.eq(column.commitStatusId, entity.commitStatusId)
    }.update.apply()
    entity
  }

  def destroy(entity: CommitStatus)(implicit session: DBSession = autoSession): Unit = {
    withSQL { delete.from(CommitStatus).where.eq(column.commitStatusId, entity.commitStatusId) }.update.apply()
  }

}
