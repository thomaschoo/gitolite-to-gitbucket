package models.gitbucket

import java.sql.Clob

import org.joda.time.DateTime

import scalikejdbc._

case class IssueComment(
  userName: String,
  repositoryName: String,
  issueId: Int,
  commentId: Int,
  action: String,
  commentedUserName: String,
  content: Clob,
  registeredDate: DateTime,
  updatedDate: DateTime) {

  def save()(implicit session: DBSession = IssueComment.autoSession): IssueComment = IssueComment.save(this)(session)

  def destroy()(implicit session: DBSession = IssueComment.autoSession): Unit = IssueComment.destroy(this)(session)

}


object IssueComment extends SQLSyntaxSupport[IssueComment] {

  override val tableName = "ISSUE_COMMENT"

  override val columns = Seq("USER_NAME", "REPOSITORY_NAME", "ISSUE_ID", "COMMENT_ID", "ACTION", "COMMENTED_USER_NAME", "CONTENT", "REGISTERED_DATE", "UPDATED_DATE")

  def apply(ic: SyntaxProvider[IssueComment])(rs: WrappedResultSet): IssueComment = apply(ic.resultName)(rs)
  def apply(ic: ResultName[IssueComment])(rs: WrappedResultSet): IssueComment = new IssueComment(
    userName = rs.get(ic.userName),
    repositoryName = rs.get(ic.repositoryName),
    issueId = rs.get(ic.issueId),
    commentId = rs.get(ic.commentId),
    action = rs.get(ic.action),
    commentedUserName = rs.get(ic.commentedUserName),
    content = rs.get(ic.content),
    registeredDate = rs.get(ic.registeredDate),
    updatedDate = rs.get(ic.updatedDate)
  )

  val ic = IssueComment.syntax("ic")

  override val autoSession = AutoSession

  def find(commentId: Int)(implicit session: DBSession = autoSession): Option[IssueComment] = {
    withSQL {
      select.from(IssueComment as ic).where.eq(ic.commentId, commentId)
    }.map(IssueComment(ic.resultName)).single.apply()
  }

  def findAll()(implicit session: DBSession = autoSession): List[IssueComment] = {
    withSQL(select.from(IssueComment as ic)).map(IssueComment(ic.resultName)).list.apply()
  }

  def countAll()(implicit session: DBSession = autoSession): Long = {
    withSQL(select(sqls.count).from(IssueComment as ic)).map(rs => rs.long(1)).single.apply().get
  }

  def findBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Option[IssueComment] = {
    withSQL {
      select.from(IssueComment as ic).where.append(where)
    }.map(IssueComment(ic.resultName)).single.apply()
  }

  def findAllBy(where: SQLSyntax)(implicit session: DBSession = autoSession): List[IssueComment] = {
    withSQL {
      select.from(IssueComment as ic).where.append(where)
    }.map(IssueComment(ic.resultName)).list.apply()
  }

  def countBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Long = {
    withSQL {
      select(sqls.count).from(IssueComment as ic).where.append(where)
    }.map(_.long(1)).single.apply().get
  }

  def create(
    userName: String,
    repositoryName: String,
    issueId: Int,
    action: String,
    commentedUserName: String,
    content: Clob,
    registeredDate: DateTime,
    updatedDate: DateTime)(implicit session: DBSession = autoSession): IssueComment = {
    val generatedKey = withSQL {
      insert.into(IssueComment).columns(
        column.userName,
        column.repositoryName,
        column.issueId,
        column.action,
        column.commentedUserName,
        column.content,
        column.registeredDate,
        column.updatedDate
      ).values(
        userName,
        repositoryName,
        issueId,
        action,
        commentedUserName,
        content,
        registeredDate,
        updatedDate
      )
    }.updateAndReturnGeneratedKey.apply()

    IssueComment(
      commentId = generatedKey.toInt,
      userName = userName,
      repositoryName = repositoryName,
      issueId = issueId,
      action = action,
      commentedUserName = commentedUserName,
      content = content,
      registeredDate = registeredDate,
      updatedDate = updatedDate)
  }

  def save(entity: IssueComment)(implicit session: DBSession = autoSession): IssueComment = {
    withSQL {
      update(IssueComment).set(
        column.userName -> entity.userName,
        column.repositoryName -> entity.repositoryName,
        column.issueId -> entity.issueId,
        column.commentId -> entity.commentId,
        column.action -> entity.action,
        column.commentedUserName -> entity.commentedUserName,
        column.content -> entity.content,
        column.registeredDate -> entity.registeredDate,
        column.updatedDate -> entity.updatedDate
      ).where.eq(column.commentId, entity.commentId)
    }.update.apply()
    entity
  }

  def destroy(entity: IssueComment)(implicit session: DBSession = autoSession): Unit = {
    withSQL { delete.from(IssueComment).where.eq(column.commentId, entity.commentId) }.update.apply()
  }

}
