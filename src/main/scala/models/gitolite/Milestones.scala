package models.gitolite

import org.joda.time.{DateTime, LocalDate}

import scalikejdbc._

case class Milestones(
  id: Int,
  title: String,
  projectId: Int,
  description: Option[String] = None,
  dueDate: Option[LocalDate] = None,
  closed: Boolean,
  createdAt: DateTime,
  updatedAt: DateTime) {

  def save()(implicit session: DBSession = Milestones.autoSession): Milestones = Milestones.save(this)(session)

  def destroy()(implicit session: DBSession = Milestones.autoSession): Unit = Milestones.destroy(this)(session)

}


object Milestones extends SQLSyntaxSupport[Milestones] {

  override val tableName = "milestones"

  override val columns = Seq("id", "title", "project_id", "description", "due_date", "closed", "created_at", "updated_at")

  def apply(m: SyntaxProvider[Milestones])(rs: WrappedResultSet): Milestones = apply(m.resultName)(rs)
  def apply(m: ResultName[Milestones])(rs: WrappedResultSet): Milestones = new Milestones(
    id = rs.get(m.id),
    title = rs.get(m.title),
    projectId = rs.get(m.projectId),
    description = rs.get(m.description),
    dueDate = rs.get(m.dueDate),
    closed = rs.get(m.closed),
    createdAt = rs.get(m.createdAt),
    updatedAt = rs.get(m.updatedAt)
  )

  val m = Milestones.syntax("m")

  override val autoSession = AutoSession

  def find(id: Int)(implicit session: DBSession = autoSession): Option[Milestones] = {
    withSQL {
      select.from(Milestones as m).where.eq(m.id, id)
    }.map(Milestones(m.resultName)).single.apply()
  }

  def findAll()(implicit session: DBSession = autoSession): List[Milestones] = {
    withSQL(select.from(Milestones as m)).map(Milestones(m.resultName)).list.apply()
  }

  def countAll()(implicit session: DBSession = autoSession): Long = {
    withSQL(select(sqls.count).from(Milestones as m)).map(rs => rs.long(1)).single.apply().get
  }

  def findBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Option[Milestones] = {
    withSQL {
      select.from(Milestones as m).where.append(where)
    }.map(Milestones(m.resultName)).single.apply()
  }

  def findAllBy(where: SQLSyntax)(implicit session: DBSession = autoSession): List[Milestones] = {
    withSQL {
      select.from(Milestones as m).where.append(where)
    }.map(Milestones(m.resultName)).list.apply()
  }

  def countBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Long = {
    withSQL {
      select(sqls.count).from(Milestones as m).where.append(where)
    }.map(_.long(1)).single.apply().get
  }

  def create(
    title: String,
    projectId: Int,
    description: Option[String] = None,
    dueDate: Option[LocalDate] = None,
    closed: Boolean,
    createdAt: DateTime,
    updatedAt: DateTime)(implicit session: DBSession = autoSession): Milestones = {
    val generatedKey = withSQL {
      insert.into(Milestones).columns(
        column.title,
        column.projectId,
        column.description,
        column.dueDate,
        column.closed,
        column.createdAt,
        column.updatedAt
      ).values(
        title,
        projectId,
        description,
        dueDate,
        closed,
        createdAt,
        updatedAt
      )
    }.updateAndReturnGeneratedKey.apply()

    Milestones(
      id = generatedKey.toInt,
      title = title,
      projectId = projectId,
      description = description,
      dueDate = dueDate,
      closed = closed,
      createdAt = createdAt,
      updatedAt = updatedAt)
  }

  def save(entity: Milestones)(implicit session: DBSession = autoSession): Milestones = {
    withSQL {
      update(Milestones).set(
        column.id -> entity.id,
        column.title -> entity.title,
        column.projectId -> entity.projectId,
        column.description -> entity.description,
        column.dueDate -> entity.dueDate,
        column.closed -> entity.closed,
        column.createdAt -> entity.createdAt,
        column.updatedAt -> entity.updatedAt
      ).where.eq(column.id, entity.id)
    }.update.apply()
    entity
  }

  def destroy(entity: Milestones)(implicit session: DBSession = autoSession): Unit = {
    withSQL { delete.from(Milestones).where.eq(column.id, entity.id) }.update.apply()
  }

}
