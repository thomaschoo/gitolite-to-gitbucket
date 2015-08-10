package models.gitolite

import org.joda.time.DateTime

import scalikejdbc._

case class Sessions(
  id: Int,
  sessionId: String,
  data: Option[String] = None,
  createdAt: DateTime,
  updatedAt: DateTime) {

  def save()(implicit session: DBSession = Sessions.autoSession): Sessions = Sessions.save(this)(session)

  def destroy()(implicit session: DBSession = Sessions.autoSession): Unit = Sessions.destroy(this)(session)

}


object Sessions extends SQLSyntaxSupport[Sessions] {

  override val tableName = "sessions"

  override val columns = Seq("id", "session_id", "data", "created_at", "updated_at")

  def apply(s: SyntaxProvider[Sessions])(rs: WrappedResultSet): Sessions = apply(s.resultName)(rs)
  def apply(s: ResultName[Sessions])(rs: WrappedResultSet): Sessions = new Sessions(
    id = rs.get(s.id),
    sessionId = rs.get(s.sessionId),
    data = rs.get(s.data),
    createdAt = rs.get(s.createdAt),
    updatedAt = rs.get(s.updatedAt)
  )

  val s = Sessions.syntax("s")

  override val autoSession = AutoSession

  def find(id: Int)(implicit session: DBSession = autoSession): Option[Sessions] = {
    withSQL {
      select.from(Sessions as s).where.eq(s.id, id)
    }.map(Sessions(s.resultName)).single.apply()
  }

  def findAll()(implicit session: DBSession = autoSession): List[Sessions] = {
    withSQL(select.from(Sessions as s)).map(Sessions(s.resultName)).list.apply()
  }

  def countAll()(implicit session: DBSession = autoSession): Long = {
    withSQL(select(sqls.count).from(Sessions as s)).map(rs => rs.long(1)).single.apply().get
  }

  def findBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Option[Sessions] = {
    withSQL {
      select.from(Sessions as s).where.append(where)
    }.map(Sessions(s.resultName)).single.apply()
  }

  def findAllBy(where: SQLSyntax)(implicit session: DBSession = autoSession): List[Sessions] = {
    withSQL {
      select.from(Sessions as s).where.append(where)
    }.map(Sessions(s.resultName)).list.apply()
  }

  def countBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Long = {
    withSQL {
      select(sqls.count).from(Sessions as s).where.append(where)
    }.map(_.long(1)).single.apply().get
  }

  def create(
    sessionId: String,
    data: Option[String] = None,
    createdAt: DateTime,
    updatedAt: DateTime)(implicit session: DBSession = autoSession): Sessions = {
    val generatedKey = withSQL {
      insert.into(Sessions).columns(
        column.sessionId,
        column.data,
        column.createdAt,
        column.updatedAt
      ).values(
        sessionId,
        data,
        createdAt,
        updatedAt
      )
    }.updateAndReturnGeneratedKey.apply()

    Sessions(
      id = generatedKey.toInt,
      sessionId = sessionId,
      data = data,
      createdAt = createdAt,
      updatedAt = updatedAt)
  }

  def save(entity: Sessions)(implicit session: DBSession = autoSession): Sessions = {
    withSQL {
      update(Sessions).set(
        column.id -> entity.id,
        column.sessionId -> entity.sessionId,
        column.data -> entity.data,
        column.createdAt -> entity.createdAt,
        column.updatedAt -> entity.updatedAt
      ).where.eq(column.id, entity.id)
    }.update.apply()
    entity
  }

  def destroy(entity: Sessions)(implicit session: DBSession = autoSession): Unit = {
    withSQL { delete.from(Sessions).where.eq(column.id, entity.id) }.update.apply()
  }

}
