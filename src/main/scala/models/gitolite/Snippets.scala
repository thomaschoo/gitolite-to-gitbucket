package models.gitolite

import org.joda.time.DateTime

import scalikejdbc._

case class Snippets(
  id: Int,
  title: Option[String] = None,
  content: Option[String] = None,
  authorId: Int,
  projectId: Int,
  createdAt: DateTime,
  updatedAt: DateTime,
  fileName: Option[String] = None,
  expiresAt: Option[DateTime] = None) {

  def save()(implicit session: DBSession = Snippets.autoSession): Snippets = Snippets.save(this)(session)

  def destroy()(implicit session: DBSession = Snippets.autoSession): Unit = Snippets.destroy(this)(session)

}


object Snippets extends SQLSyntaxSupport[Snippets] {

  override val tableName = "snippets"

  override val columns = Seq("id", "title", "content", "author_id", "project_id", "created_at", "updated_at", "file_name", "expires_at")

  def apply(s: SyntaxProvider[Snippets])(rs: WrappedResultSet): Snippets = apply(s.resultName)(rs)
  def apply(s: ResultName[Snippets])(rs: WrappedResultSet): Snippets = new Snippets(
    id = rs.get(s.id),
    title = rs.get(s.title),
    content = rs.get(s.content),
    authorId = rs.get(s.authorId),
    projectId = rs.get(s.projectId),
    createdAt = rs.get(s.createdAt),
    updatedAt = rs.get(s.updatedAt),
    fileName = rs.get(s.fileName),
    expiresAt = rs.get(s.expiresAt)
  )

  val s = Snippets.syntax("s")

  override val autoSession = AutoSession

  def find(id: Int)(implicit session: DBSession = autoSession): Option[Snippets] = {
    withSQL {
      select.from(Snippets as s).where.eq(s.id, id)
    }.map(Snippets(s.resultName)).single.apply()
  }

  def findAll()(implicit session: DBSession = autoSession): List[Snippets] = {
    withSQL(select.from(Snippets as s)).map(Snippets(s.resultName)).list.apply()
  }

  def countAll()(implicit session: DBSession = autoSession): Long = {
    withSQL(select(sqls.count).from(Snippets as s)).map(rs => rs.long(1)).single.apply().get
  }

  def findBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Option[Snippets] = {
    withSQL {
      select.from(Snippets as s).where.append(where)
    }.map(Snippets(s.resultName)).single.apply()
  }

  def findAllBy(where: SQLSyntax)(implicit session: DBSession = autoSession): List[Snippets] = {
    withSQL {
      select.from(Snippets as s).where.append(where)
    }.map(Snippets(s.resultName)).list.apply()
  }

  def countBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Long = {
    withSQL {
      select(sqls.count).from(Snippets as s).where.append(where)
    }.map(_.long(1)).single.apply().get
  }

  def create(
    title: Option[String] = None,
    content: Option[String] = None,
    authorId: Int,
    projectId: Int,
    createdAt: DateTime,
    updatedAt: DateTime,
    fileName: Option[String] = None,
    expiresAt: Option[DateTime] = None)(implicit session: DBSession = autoSession): Snippets = {
    val generatedKey = withSQL {
      insert.into(Snippets).columns(
        column.title,
        column.content,
        column.authorId,
        column.projectId,
        column.createdAt,
        column.updatedAt,
        column.fileName,
        column.expiresAt
      ).values(
        title,
        content,
        authorId,
        projectId,
        createdAt,
        updatedAt,
        fileName,
        expiresAt
      )
    }.updateAndReturnGeneratedKey.apply()

    Snippets(
      id = generatedKey.toInt,
      title = title,
      content = content,
      authorId = authorId,
      projectId = projectId,
      createdAt = createdAt,
      updatedAt = updatedAt,
      fileName = fileName,
      expiresAt = expiresAt)
  }

  def save(entity: Snippets)(implicit session: DBSession = autoSession): Snippets = {
    withSQL {
      update(Snippets).set(
        column.id -> entity.id,
        column.title -> entity.title,
        column.content -> entity.content,
        column.authorId -> entity.authorId,
        column.projectId -> entity.projectId,
        column.createdAt -> entity.createdAt,
        column.updatedAt -> entity.updatedAt,
        column.fileName -> entity.fileName,
        column.expiresAt -> entity.expiresAt
      ).where.eq(column.id, entity.id)
    }.update.apply()
    entity
  }

  def destroy(entity: Snippets)(implicit session: DBSession = autoSession): Unit = {
    withSQL { delete.from(Snippets).where.eq(column.id, entity.id) }.update.apply()
  }

}
