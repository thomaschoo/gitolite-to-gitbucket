package models.gitolite

import org.joda.time.DateTime

import scalikejdbc._

case class Events(
  id: Int,
  targetType: Option[String] = None,
  targetId: Option[Int] = None,
  title: Option[String] = None,
  data: Option[String] = None,
  projectId: Option[Int] = None,
  createdAt: DateTime,
  updatedAt: DateTime,
  action: Option[Int] = None,
  authorId: Option[Int] = None) {

  def save()(implicit session: DBSession = Events.autoSession): Events = Events.save(this)(session)

  def destroy()(implicit session: DBSession = Events.autoSession): Unit = Events.destroy(this)(session)

}


object Events extends SQLSyntaxSupport[Events] {

  override val tableName = "events"

  override val columns = Seq("id", "target_type", "target_id", "title", "data", "project_id", "created_at", "updated_at", "action", "author_id")

  def apply(e: SyntaxProvider[Events])(rs: WrappedResultSet): Events = apply(e.resultName)(rs)
  def apply(e: ResultName[Events])(rs: WrappedResultSet): Events = new Events(
    id = rs.get(e.id),
    targetType = rs.get(e.targetType),
    targetId = rs.get(e.targetId),
    title = rs.get(e.title),
    data = rs.get(e.data),
    projectId = rs.get(e.projectId),
    createdAt = rs.get(e.createdAt),
    updatedAt = rs.get(e.updatedAt),
    action = rs.get(e.action),
    authorId = rs.get(e.authorId)
  )

  val e = Events.syntax("e")

  override val autoSession = AutoSession

  def find(id: Int)(implicit session: DBSession = autoSession): Option[Events] = {
    withSQL {
      select.from(Events as e).where.eq(e.id, id)
    }.map(Events(e.resultName)).single.apply()
  }

  def findAll()(implicit session: DBSession = autoSession): List[Events] = {
    withSQL(select.from(Events as e)).map(Events(e.resultName)).list.apply()
  }

  def countAll()(implicit session: DBSession = autoSession): Long = {
    withSQL(select(sqls.count).from(Events as e)).map(rs => rs.long(1)).single.apply().get
  }

  def findBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Option[Events] = {
    withSQL {
      select.from(Events as e).where.append(where)
    }.map(Events(e.resultName)).single.apply()
  }

  def findAllBy(where: SQLSyntax)(implicit session: DBSession = autoSession): List[Events] = {
    withSQL {
      select.from(Events as e).where.append(where)
    }.map(Events(e.resultName)).list.apply()
  }

  def countBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Long = {
    withSQL {
      select(sqls.count).from(Events as e).where.append(where)
    }.map(_.long(1)).single.apply().get
  }

  def create(
    targetType: Option[String] = None,
    targetId: Option[Int] = None,
    title: Option[String] = None,
    data: Option[String] = None,
    projectId: Option[Int] = None,
    createdAt: DateTime,
    updatedAt: DateTime,
    action: Option[Int] = None,
    authorId: Option[Int] = None)(implicit session: DBSession = autoSession): Events = {
    val generatedKey = withSQL {
      insert.into(Events).columns(
        column.targetType,
        column.targetId,
        column.title,
        column.data,
        column.projectId,
        column.createdAt,
        column.updatedAt,
        column.action,
        column.authorId
      ).values(
        targetType,
        targetId,
        title,
        data,
        projectId,
        createdAt,
        updatedAt,
        action,
        authorId
      )
    }.updateAndReturnGeneratedKey.apply()

    Events(
      id = generatedKey.toInt,
      targetType = targetType,
      targetId = targetId,
      title = title,
      data = data,
      projectId = projectId,
      createdAt = createdAt,
      updatedAt = updatedAt,
      action = action,
      authorId = authorId)
  }

  def save(entity: Events)(implicit session: DBSession = autoSession): Events = {
    withSQL {
      update(Events).set(
        column.id -> entity.id,
        column.targetType -> entity.targetType,
        column.targetId -> entity.targetId,
        column.title -> entity.title,
        column.data -> entity.data,
        column.projectId -> entity.projectId,
        column.createdAt -> entity.createdAt,
        column.updatedAt -> entity.updatedAt,
        column.action -> entity.action,
        column.authorId -> entity.authorId
      ).where.eq(column.id, entity.id)
    }.update.apply()
    entity
  }

  def destroy(entity: Events)(implicit session: DBSession = autoSession): Unit = {
    withSQL { delete.from(Events).where.eq(column.id, entity.id) }.update.apply()
  }

}
