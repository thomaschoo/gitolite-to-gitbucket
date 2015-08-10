package models.gitbucket

import java.sql.Clob

import org.joda.time.DateTime

import scalikejdbc._

case class CommitComment(
  userName: String,
  repositoryName: String,
  commitId: String,
  commentId: Int,
  commentedUserName: String,
  content: Clob,
  fileName: Option[String] = None,
  oldLineNumber: Option[Int] = None,
  newLineNumber: Option[Int] = None,
  registeredDate: DateTime,
  updatedDate: DateTime,
  pullRequest: Boolean) {

  def save()(implicit session: DBSession = CommitComment.autoSession): CommitComment = CommitComment.save(this)(session)

  def destroy()(implicit session: DBSession = CommitComment.autoSession): Unit = CommitComment.destroy(this)(session)

}


object CommitComment extends SQLSyntaxSupport[CommitComment] {

  override val tableName = "COMMIT_COMMENT"

  override val columns = Seq("USER_NAME", "REPOSITORY_NAME", "COMMIT_ID", "COMMENT_ID", "COMMENTED_USER_NAME", "CONTENT", "FILE_NAME", "OLD_LINE_NUMBER", "NEW_LINE_NUMBER", "REGISTERED_DATE", "UPDATED_DATE", "PULL_REQUEST")

  def apply(cc: SyntaxProvider[CommitComment])(rs: WrappedResultSet): CommitComment = apply(cc.resultName)(rs)
  def apply(cc: ResultName[CommitComment])(rs: WrappedResultSet): CommitComment = new CommitComment(
    userName = rs.get(cc.userName),
    repositoryName = rs.get(cc.repositoryName),
    commitId = rs.get(cc.commitId),
    commentId = rs.get(cc.commentId),
    commentedUserName = rs.get(cc.commentedUserName),
    content = rs.get(cc.content),
    fileName = rs.get(cc.fileName),
    oldLineNumber = rs.get(cc.oldLineNumber),
    newLineNumber = rs.get(cc.newLineNumber),
    registeredDate = rs.get(cc.registeredDate),
    updatedDate = rs.get(cc.updatedDate),
    pullRequest = rs.get(cc.pullRequest)
  )

  val cc = CommitComment.syntax("cc")

  override val autoSession = AutoSession

  def find(commentId: Int)(implicit session: DBSession = autoSession): Option[CommitComment] = {
    withSQL {
      select.from(CommitComment as cc).where.eq(cc.commentId, commentId)
    }.map(CommitComment(cc.resultName)).single.apply()
  }

  def findAll()(implicit session: DBSession = autoSession): List[CommitComment] = {
    withSQL(select.from(CommitComment as cc)).map(CommitComment(cc.resultName)).list.apply()
  }

  def countAll()(implicit session: DBSession = autoSession): Long = {
    withSQL(select(sqls.count).from(CommitComment as cc)).map(rs => rs.long(1)).single.apply().get
  }

  def findBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Option[CommitComment] = {
    withSQL {
      select.from(CommitComment as cc).where.append(where)
    }.map(CommitComment(cc.resultName)).single.apply()
  }

  def findAllBy(where: SQLSyntax)(implicit session: DBSession = autoSession): List[CommitComment] = {
    withSQL {
      select.from(CommitComment as cc).where.append(where)
    }.map(CommitComment(cc.resultName)).list.apply()
  }

  def countBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Long = {
    withSQL {
      select(sqls.count).from(CommitComment as cc).where.append(where)
    }.map(_.long(1)).single.apply().get
  }

  def create(
    userName: String,
    repositoryName: String,
    commitId: String,
    commentedUserName: String,
    content: Clob,
    fileName: Option[String] = None,
    oldLineNumber: Option[Int] = None,
    newLineNumber: Option[Int] = None,
    registeredDate: DateTime,
    updatedDate: DateTime,
    pullRequest: Boolean)(implicit session: DBSession = autoSession): CommitComment = {
    val generatedKey = withSQL {
      insert.into(CommitComment).columns(
        column.userName,
        column.repositoryName,
        column.commitId,
        column.commentedUserName,
        column.content,
        column.fileName,
        column.oldLineNumber,
        column.newLineNumber,
        column.registeredDate,
        column.updatedDate,
        column.pullRequest
      ).values(
        userName,
        repositoryName,
        commitId,
        commentedUserName,
        content,
        fileName,
        oldLineNumber,
        newLineNumber,
        registeredDate,
        updatedDate,
        pullRequest
      )
    }.updateAndReturnGeneratedKey.apply()

    CommitComment(
      commentId = generatedKey.toInt,
      userName = userName,
      repositoryName = repositoryName,
      commitId = commitId,
      commentedUserName = commentedUserName,
      content = content,
      fileName = fileName,
      oldLineNumber = oldLineNumber,
      newLineNumber = newLineNumber,
      registeredDate = registeredDate,
      updatedDate = updatedDate,
      pullRequest = pullRequest)
  }

  def save(entity: CommitComment)(implicit session: DBSession = autoSession): CommitComment = {
    withSQL {
      update(CommitComment).set(
        column.userName -> entity.userName,
        column.repositoryName -> entity.repositoryName,
        column.commitId -> entity.commitId,
        column.commentId -> entity.commentId,
        column.commentedUserName -> entity.commentedUserName,
        column.content -> entity.content,
        column.fileName -> entity.fileName,
        column.oldLineNumber -> entity.oldLineNumber,
        column.newLineNumber -> entity.newLineNumber,
        column.registeredDate -> entity.registeredDate,
        column.updatedDate -> entity.updatedDate,
        column.pullRequest -> entity.pullRequest
      ).where.eq(column.commentId, entity.commentId)
    }.update.apply()
    entity
  }

  def destroy(entity: CommitComment)(implicit session: DBSession = autoSession): Unit = {
    withSQL { delete.from(CommitComment).where.eq(column.commentId, entity.commentId) }.update.apply()
  }

}
