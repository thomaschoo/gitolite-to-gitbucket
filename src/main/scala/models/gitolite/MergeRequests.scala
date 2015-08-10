package models.gitolite

import org.joda.time.DateTime

import scalikejdbc._

case class MergeRequests(
  id: Int,
  targetBranch: String,
  sourceBranch: String,
  projectId: Int,
  authorId: Option[Int] = None,
  assigneeId: Option[Int] = None,
  title: Option[String] = None,
  closed: Boolean,
  createdAt: DateTime,
  updatedAt: DateTime,
  stCommits: Option[String] = None,
  stDiffs: Option[String] = None,
  merged: Boolean,
  state: Int) {

  def save()(implicit session: DBSession = MergeRequests.autoSession): MergeRequests = MergeRequests.save(this)(session)

  def destroy()(implicit session: DBSession = MergeRequests.autoSession): Unit = MergeRequests.destroy(this)(session)

}


object MergeRequests extends SQLSyntaxSupport[MergeRequests] {

  override val tableName = "merge_requests"

  override val columns = Seq("id", "target_branch", "source_branch", "project_id", "author_id", "assignee_id", "title", "closed", "created_at", "updated_at", "st_commits", "st_diffs", "merged", "state")

  def apply(mr: SyntaxProvider[MergeRequests])(rs: WrappedResultSet): MergeRequests = apply(mr.resultName)(rs)
  def apply(mr: ResultName[MergeRequests])(rs: WrappedResultSet): MergeRequests = new MergeRequests(
    id = rs.get(mr.id),
    targetBranch = rs.get(mr.targetBranch),
    sourceBranch = rs.get(mr.sourceBranch),
    projectId = rs.get(mr.projectId),
    authorId = rs.get(mr.authorId),
    assigneeId = rs.get(mr.assigneeId),
    title = rs.get(mr.title),
    closed = rs.get(mr.closed),
    createdAt = rs.get(mr.createdAt),
    updatedAt = rs.get(mr.updatedAt),
    stCommits = rs.get(mr.stCommits),
    stDiffs = rs.get(mr.stDiffs),
    merged = rs.get(mr.merged),
    state = rs.get(mr.state)
  )

  val mr = MergeRequests.syntax("mr")

  override val autoSession = AutoSession

  def find(id: Int)(implicit session: DBSession = autoSession): Option[MergeRequests] = {
    withSQL {
      select.from(MergeRequests as mr).where.eq(mr.id, id)
    }.map(MergeRequests(mr.resultName)).single.apply()
  }

  def findAll()(implicit session: DBSession = autoSession): List[MergeRequests] = {
    withSQL(select.from(MergeRequests as mr)).map(MergeRequests(mr.resultName)).list.apply()
  }

  def countAll()(implicit session: DBSession = autoSession): Long = {
    withSQL(select(sqls.count).from(MergeRequests as mr)).map(rs => rs.long(1)).single.apply().get
  }

  def findBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Option[MergeRequests] = {
    withSQL {
      select.from(MergeRequests as mr).where.append(where)
    }.map(MergeRequests(mr.resultName)).single.apply()
  }

  def findAllBy(where: SQLSyntax)(implicit session: DBSession = autoSession): List[MergeRequests] = {
    withSQL {
      select.from(MergeRequests as mr).where.append(where)
    }.map(MergeRequests(mr.resultName)).list.apply()
  }

  def countBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Long = {
    withSQL {
      select(sqls.count).from(MergeRequests as mr).where.append(where)
    }.map(_.long(1)).single.apply().get
  }

  def create(
    targetBranch: String,
    sourceBranch: String,
    projectId: Int,
    authorId: Option[Int] = None,
    assigneeId: Option[Int] = None,
    title: Option[String] = None,
    closed: Boolean,
    createdAt: DateTime,
    updatedAt: DateTime,
    stCommits: Option[String] = None,
    stDiffs: Option[String] = None,
    merged: Boolean,
    state: Int)(implicit session: DBSession = autoSession): MergeRequests = {
    val generatedKey = withSQL {
      insert.into(MergeRequests).columns(
        column.targetBranch,
        column.sourceBranch,
        column.projectId,
        column.authorId,
        column.assigneeId,
        column.title,
        column.closed,
        column.createdAt,
        column.updatedAt,
        column.stCommits,
        column.stDiffs,
        column.merged,
        column.state
      ).values(
        targetBranch,
        sourceBranch,
        projectId,
        authorId,
        assigneeId,
        title,
        closed,
        createdAt,
        updatedAt,
        stCommits,
        stDiffs,
        merged,
        state
      )
    }.updateAndReturnGeneratedKey.apply()

    MergeRequests(
      id = generatedKey.toInt,
      targetBranch = targetBranch,
      sourceBranch = sourceBranch,
      projectId = projectId,
      authorId = authorId,
      assigneeId = assigneeId,
      title = title,
      closed = closed,
      createdAt = createdAt,
      updatedAt = updatedAt,
      stCommits = stCommits,
      stDiffs = stDiffs,
      merged = merged,
      state = state)
  }

  def save(entity: MergeRequests)(implicit session: DBSession = autoSession): MergeRequests = {
    withSQL {
      update(MergeRequests).set(
        column.id -> entity.id,
        column.targetBranch -> entity.targetBranch,
        column.sourceBranch -> entity.sourceBranch,
        column.projectId -> entity.projectId,
        column.authorId -> entity.authorId,
        column.assigneeId -> entity.assigneeId,
        column.title -> entity.title,
        column.closed -> entity.closed,
        column.createdAt -> entity.createdAt,
        column.updatedAt -> entity.updatedAt,
        column.stCommits -> entity.stCommits,
        column.stDiffs -> entity.stDiffs,
        column.merged -> entity.merged,
        column.state -> entity.state
      ).where.eq(column.id, entity.id)
    }.update.apply()
    entity
  }

  def destroy(entity: MergeRequests)(implicit session: DBSession = autoSession): Unit = {
    withSQL { delete.from(MergeRequests).where.eq(column.id, entity.id) }.update.apply()
  }

}
