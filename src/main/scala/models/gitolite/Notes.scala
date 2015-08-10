package models.gitolite

import org.joda.time.DateTime

import scalikejdbc._

case class Notes(
  id: Int,
  note: Option[String] = None,
  noteableId: Option[String] = None,
  noteableType: Option[String] = None,
  authorId: Option[Int] = None,
  createdAt: DateTime,
  updatedAt: DateTime,
  projectId: Option[Int] = None,
  attachment: Option[String] = None,
  lineCode: Option[String] = None) {

  def save()(implicit session: DBSession = Notes.autoSession): Notes = Notes.save(this)(session)

  def destroy()(implicit session: DBSession = Notes.autoSession): Unit = Notes.destroy(this)(session)

}


object Notes extends SQLSyntaxSupport[Notes] {

  override val tableName = "notes"

  override val columns = Seq("id", "note", "noteable_id", "noteable_type", "author_id", "created_at", "updated_at", "project_id", "attachment", "line_code")

  def apply(n: SyntaxProvider[Notes])(rs: WrappedResultSet): Notes = apply(n.resultName)(rs)
  def apply(n: ResultName[Notes])(rs: WrappedResultSet): Notes = new Notes(
    id = rs.get(n.id),
    note = rs.get(n.note),
    noteableId = rs.get(n.noteableId),
    noteableType = rs.get(n.noteableType),
    authorId = rs.get(n.authorId),
    createdAt = rs.get(n.createdAt),
    updatedAt = rs.get(n.updatedAt),
    projectId = rs.get(n.projectId),
    attachment = rs.get(n.attachment),
    lineCode = rs.get(n.lineCode)
  )

  val n = Notes.syntax("n")

  override val autoSession = AutoSession

  def find(id: Int)(implicit session: DBSession = autoSession): Option[Notes] = {
    withSQL {
      select.from(Notes as n).where.eq(n.id, id)
    }.map(Notes(n.resultName)).single.apply()
  }

  def findAll()(implicit session: DBSession = autoSession): List[Notes] = {
    withSQL(select.from(Notes as n)).map(Notes(n.resultName)).list.apply()
  }

  def countAll()(implicit session: DBSession = autoSession): Long = {
    withSQL(select(sqls.count).from(Notes as n)).map(rs => rs.long(1)).single.apply().get
  }

  def findBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Option[Notes] = {
    withSQL {
      select.from(Notes as n).where.append(where)
    }.map(Notes(n.resultName)).single.apply()
  }

  def findAllBy(where: SQLSyntax)(implicit session: DBSession = autoSession): List[Notes] = {
    withSQL {
      select.from(Notes as n).where.append(where)
    }.map(Notes(n.resultName)).list.apply()
  }

  def countBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Long = {
    withSQL {
      select(sqls.count).from(Notes as n).where.append(where)
    }.map(_.long(1)).single.apply().get
  }

  def create(
    note: Option[String] = None,
    noteableId: Option[String] = None,
    noteableType: Option[String] = None,
    authorId: Option[Int] = None,
    createdAt: DateTime,
    updatedAt: DateTime,
    projectId: Option[Int] = None,
    attachment: Option[String] = None,
    lineCode: Option[String] = None)(implicit session: DBSession = autoSession): Notes = {
    val generatedKey = withSQL {
      insert.into(Notes).columns(
        column.note,
        column.noteableId,
        column.noteableType,
        column.authorId,
        column.createdAt,
        column.updatedAt,
        column.projectId,
        column.attachment,
        column.lineCode
      ).values(
        note,
        noteableId,
        noteableType,
        authorId,
        createdAt,
        updatedAt,
        projectId,
        attachment,
        lineCode
      )
    }.updateAndReturnGeneratedKey.apply()

    Notes(
      id = generatedKey.toInt,
      note = note,
      noteableId = noteableId,
      noteableType = noteableType,
      authorId = authorId,
      createdAt = createdAt,
      updatedAt = updatedAt,
      projectId = projectId,
      attachment = attachment,
      lineCode = lineCode)
  }

  def save(entity: Notes)(implicit session: DBSession = autoSession): Notes = {
    withSQL {
      update(Notes).set(
        column.id -> entity.id,
        column.note -> entity.note,
        column.noteableId -> entity.noteableId,
        column.noteableType -> entity.noteableType,
        column.authorId -> entity.authorId,
        column.createdAt -> entity.createdAt,
        column.updatedAt -> entity.updatedAt,
        column.projectId -> entity.projectId,
        column.attachment -> entity.attachment,
        column.lineCode -> entity.lineCode
      ).where.eq(column.id, entity.id)
    }.update.apply()
    entity
  }

  def destroy(entity: Notes)(implicit session: DBSession = autoSession): Unit = {
    withSQL { delete.from(Notes).where.eq(column.id, entity.id) }.update.apply()
  }

}
