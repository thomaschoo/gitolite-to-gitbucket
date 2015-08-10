package models.gitbucket

import java.sql.Clob

import org.joda.time.DateTime

import scalikejdbc._

case class Issue(
  userName: String,
  repositoryName: String,
  issueId: Int,
  openedUserName: String,
  milestoneId: Option[Int] = None,
  assignedUserName: Option[String] = None,
  title: Clob,
  content: Option[Clob] = None,
  closed: Boolean,
  registeredDate: DateTime,
  updatedDate: DateTime,
  pullRequest: Boolean) {

  def save()(implicit session: DBSession = Issue.autoSession): Issue = Issue.save(this)(session)

  def destroy()(implicit session: DBSession = Issue.autoSession): Unit = Issue.destroy(this)(session)

}


object Issue extends SQLSyntaxSupport[Issue] {

  override val tableName = "ISSUE"

  override val columns = Seq("USER_NAME", "REPOSITORY_NAME", "ISSUE_ID", "OPENED_USER_NAME", "MILESTONE_ID", "ASSIGNED_USER_NAME", "TITLE", "CONTENT", "CLOSED", "REGISTERED_DATE", "UPDATED_DATE", "PULL_REQUEST")

  def apply(i: SyntaxProvider[Issue])(rs: WrappedResultSet): Issue = apply(i.resultName)(rs)
  def apply(i: ResultName[Issue])(rs: WrappedResultSet): Issue = new Issue(
    userName = rs.get(i.userName),
    repositoryName = rs.get(i.repositoryName),
    issueId = rs.get(i.issueId),
    openedUserName = rs.get(i.openedUserName),
    milestoneId = rs.get(i.milestoneId),
    assignedUserName = rs.get(i.assignedUserName),
    title = rs.get(i.title),
    content = rs.get(i.content),
    closed = rs.get(i.closed),
    registeredDate = rs.get(i.registeredDate),
    updatedDate = rs.get(i.updatedDate),
    pullRequest = rs.get(i.pullRequest)
  )

  val i = Issue.syntax("i")

  override val autoSession = AutoSession

  def find(issueId: Int, repositoryName: String, userName: String)(implicit session: DBSession = autoSession): Option[Issue] = {
    withSQL {
      select.from(Issue as i).where.eq(i.issueId, issueId).and.eq(i.repositoryName, repositoryName).and.eq(i.userName, userName)
    }.map(Issue(i.resultName)).single.apply()
  }

  def findAll()(implicit session: DBSession = autoSession): List[Issue] = {
    withSQL(select.from(Issue as i)).map(Issue(i.resultName)).list.apply()
  }

  def countAll()(implicit session: DBSession = autoSession): Long = {
    withSQL(select(sqls.count).from(Issue as i)).map(rs => rs.long(1)).single.apply().get
  }

  def findBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Option[Issue] = {
    withSQL {
      select.from(Issue as i).where.append(where)
    }.map(Issue(i.resultName)).single.apply()
  }

  def findAllBy(where: SQLSyntax)(implicit session: DBSession = autoSession): List[Issue] = {
    withSQL {
      select.from(Issue as i).where.append(where)
    }.map(Issue(i.resultName)).list.apply()
  }

  def countBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Long = {
    withSQL {
      select(sqls.count).from(Issue as i).where.append(where)
    }.map(_.long(1)).single.apply().get
  }

  def create(
    userName: String,
    repositoryName: String,
    issueId: Int,
    openedUserName: String,
    milestoneId: Option[Int] = None,
    assignedUserName: Option[String] = None,
    title: Clob,
    content: Option[Clob] = None,
    closed: Boolean,
    registeredDate: DateTime,
    updatedDate: DateTime,
    pullRequest: Boolean)(implicit session: DBSession = autoSession): Issue = {
    withSQL {
      insert.into(Issue).columns(
        column.userName,
        column.repositoryName,
        column.issueId,
        column.openedUserName,
        column.milestoneId,
        column.assignedUserName,
        column.title,
        column.content,
        column.closed,
        column.registeredDate,
        column.updatedDate,
        column.pullRequest
      ).values(
        userName,
        repositoryName,
        issueId,
        openedUserName,
        milestoneId,
        assignedUserName,
        title,
        content,
        closed,
        registeredDate,
        updatedDate,
        pullRequest
      )
    }.update.apply()

    Issue(
      userName = userName,
      repositoryName = repositoryName,
      issueId = issueId,
      openedUserName = openedUserName,
      milestoneId = milestoneId,
      assignedUserName = assignedUserName,
      title = title,
      content = content,
      closed = closed,
      registeredDate = registeredDate,
      updatedDate = updatedDate,
      pullRequest = pullRequest)
  }

  def save(entity: Issue)(implicit session: DBSession = autoSession): Issue = {
    withSQL {
      update(Issue).set(
        column.userName -> entity.userName,
        column.repositoryName -> entity.repositoryName,
        column.issueId -> entity.issueId,
        column.openedUserName -> entity.openedUserName,
        column.milestoneId -> entity.milestoneId,
        column.assignedUserName -> entity.assignedUserName,
        column.title -> entity.title,
        column.content -> entity.content,
        column.closed -> entity.closed,
        column.registeredDate -> entity.registeredDate,
        column.updatedDate -> entity.updatedDate,
        column.pullRequest -> entity.pullRequest
      ).where.eq(column.issueId, entity.issueId).and.eq(column.repositoryName, entity.repositoryName).and.eq(column.userName, entity.userName)
    }.update.apply()
    entity
  }

  def destroy(entity: Issue)(implicit session: DBSession = autoSession): Unit = {
    withSQL { delete.from(Issue).where.eq(column.issueId, entity.issueId).and.eq(column.repositoryName, entity.repositoryName).and.eq(column.userName, entity.userName) }.update.apply()
  }

}
