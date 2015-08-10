package models.gitolite

import org.joda.time.DateTime

import scalikejdbc._

case class Issues(
  id: Int,
  title: Option[String] = None,
  assigneeId: Option[Int] = None,
  authorId: Option[Int] = None,
  projectId: Option[Int] = None,
  createdAt: DateTime,
  updatedAt: DateTime,
  closed: Boolean,
  position: Option[Int] = None,
  branchName: Option[String] = None,
  description: Option[String] = None,
  milestoneId: Option[Int] = None) {

  def save()(implicit session: DBSession = Issues.autoSession): Issues = Issues.save(this)(session)

  def destroy()(implicit session: DBSession = Issues.autoSession): Unit = Issues.destroy(this)(session)

}


object Issues extends SQLSyntaxSupport[Issues] {

  override val tableName = "issues"

  override val columns = Seq("id", "title", "assignee_id", "author_id", "project_id", "created_at", "updated_at", "closed", "position", "branch_name", "description", "milestone_id")

  def apply(i: SyntaxProvider[Issues])(rs: WrappedResultSet): Issues = apply(i.resultName)(rs)
  def apply(i: ResultName[Issues])(rs: WrappedResultSet): Issues = new Issues(
    id = rs.get(i.id),
    title = rs.get(i.title),
    assigneeId = rs.get(i.assigneeId),
    authorId = rs.get(i.authorId),
    projectId = rs.get(i.projectId),
    createdAt = rs.get(i.createdAt),
    updatedAt = rs.get(i.updatedAt),
    closed = rs.get(i.closed),
    position = rs.get(i.position),
    branchName = rs.get(i.branchName),
    description = rs.get(i.description),
    milestoneId = rs.get(i.milestoneId)
  )

  val i = Issues.syntax("i")

  override val autoSession = AutoSession

  def find(id: Int)(implicit session: DBSession = autoSession): Option[Issues] = {
    withSQL {
      select.from(Issues as i).where.eq(i.id, id)
    }.map(Issues(i.resultName)).single.apply()
  }

  def findAll()(implicit session: DBSession = autoSession): List[Issues] = {
    withSQL(select.from(Issues as i)).map(Issues(i.resultName)).list.apply()
  }

  def countAll()(implicit session: DBSession = autoSession): Long = {
    withSQL(select(sqls.count).from(Issues as i)).map(rs => rs.long(1)).single.apply().get
  }

  def findBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Option[Issues] = {
    withSQL {
      select.from(Issues as i).where.append(where)
    }.map(Issues(i.resultName)).single.apply()
  }

  def findAllBy(where: SQLSyntax)(implicit session: DBSession = autoSession): List[Issues] = {
    withSQL {
      select.from(Issues as i).where.append(where)
    }.map(Issues(i.resultName)).list.apply()
  }

  def countBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Long = {
    withSQL {
      select(sqls.count).from(Issues as i).where.append(where)
    }.map(_.long(1)).single.apply().get
  }

  def create(
    title: Option[String] = None,
    assigneeId: Option[Int] = None,
    authorId: Option[Int] = None,
    projectId: Option[Int] = None,
    createdAt: DateTime,
    updatedAt: DateTime,
    closed: Boolean,
    position: Option[Int] = None,
    branchName: Option[String] = None,
    description: Option[String] = None,
    milestoneId: Option[Int] = None)(implicit session: DBSession = autoSession): Issues = {
    val generatedKey = withSQL {
      insert.into(Issues).columns(
        column.title,
        column.assigneeId,
        column.authorId,
        column.projectId,
        column.createdAt,
        column.updatedAt,
        column.closed,
        column.position,
        column.branchName,
        column.description,
        column.milestoneId
      ).values(
        title,
        assigneeId,
        authorId,
        projectId,
        createdAt,
        updatedAt,
        closed,
        position,
        branchName,
        description,
        milestoneId
      )
    }.updateAndReturnGeneratedKey.apply()

    Issues(
      id = generatedKey.toInt,
      title = title,
      assigneeId = assigneeId,
      authorId = authorId,
      projectId = projectId,
      createdAt = createdAt,
      updatedAt = updatedAt,
      closed = closed,
      position = position,
      branchName = branchName,
      description = description,
      milestoneId = milestoneId)
  }

  def save(entity: Issues)(implicit session: DBSession = autoSession): Issues = {
    withSQL {
      update(Issues).set(
        column.id -> entity.id,
        column.title -> entity.title,
        column.assigneeId -> entity.assigneeId,
        column.authorId -> entity.authorId,
        column.projectId -> entity.projectId,
        column.createdAt -> entity.createdAt,
        column.updatedAt -> entity.updatedAt,
        column.closed -> entity.closed,
        column.position -> entity.position,
        column.branchName -> entity.branchName,
        column.description -> entity.description,
        column.milestoneId -> entity.milestoneId
      ).where.eq(column.id, entity.id)
    }.update.apply()
    entity
  }

  def destroy(entity: Issues)(implicit session: DBSession = autoSession): Unit = {
    withSQL { delete.from(Issues).where.eq(column.id, entity.id) }.update.apply()
  }

}
