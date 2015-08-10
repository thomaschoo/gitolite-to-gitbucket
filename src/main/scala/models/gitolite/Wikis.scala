package models.gitolite

import org.joda.time.DateTime

import scalikejdbc._

case class Wikis(
  id: Int,
  title: Option[String] = None,
  content: Option[String] = None,
  projectId: Option[Int] = None,
  createdAt: DateTime,
  updatedAt: DateTime,
  slug: Option[String] = None,
  userId: Option[Int] = None) {

  def save()(implicit session: DBSession = Wikis.autoSession): Wikis = Wikis.save(this)(session)

  def destroy()(implicit session: DBSession = Wikis.autoSession): Unit = Wikis.destroy(this)(session)

}


object Wikis extends SQLSyntaxSupport[Wikis] {

  override val tableName = "wikis"

  override val columns = Seq("id", "title", "content", "project_id", "created_at", "updated_at", "slug", "user_id")

  def apply(w: SyntaxProvider[Wikis])(rs: WrappedResultSet): Wikis = apply(w.resultName)(rs)
  def apply(w: ResultName[Wikis])(rs: WrappedResultSet): Wikis = new Wikis(
    id = rs.get(w.id),
    title = rs.get(w.title),
    content = rs.get(w.content),
    projectId = rs.get(w.projectId),
    createdAt = rs.get(w.createdAt),
    updatedAt = rs.get(w.updatedAt),
    slug = rs.get(w.slug),
    userId = rs.get(w.userId)
  )

  val w = Wikis.syntax("w")

  override val autoSession = AutoSession

  def find(id: Int)(implicit session: DBSession = autoSession): Option[Wikis] = {
    withSQL {
      select.from(Wikis as w).where.eq(w.id, id)
    }.map(Wikis(w.resultName)).single.apply()
  }

  def findAll()(implicit session: DBSession = autoSession): List[Wikis] = {
    withSQL(select.from(Wikis as w)).map(Wikis(w.resultName)).list.apply()
  }

  def countAll()(implicit session: DBSession = autoSession): Long = {
    withSQL(select(sqls.count).from(Wikis as w)).map(rs => rs.long(1)).single.apply().get
  }

  def findBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Option[Wikis] = {
    withSQL {
      select.from(Wikis as w).where.append(where)
    }.map(Wikis(w.resultName)).single.apply()
  }

  def findAllBy(where: SQLSyntax)(implicit session: DBSession = autoSession): List[Wikis] = {
    withSQL {
      select.from(Wikis as w).where.append(where)
    }.map(Wikis(w.resultName)).list.apply()
  }

  def countBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Long = {
    withSQL {
      select(sqls.count).from(Wikis as w).where.append(where)
    }.map(_.long(1)).single.apply().get
  }

  def create(
    title: Option[String] = None,
    content: Option[String] = None,
    projectId: Option[Int] = None,
    createdAt: DateTime,
    updatedAt: DateTime,
    slug: Option[String] = None,
    userId: Option[Int] = None)(implicit session: DBSession = autoSession): Wikis = {
    val generatedKey = withSQL {
      insert.into(Wikis).columns(
        column.title,
        column.content,
        column.projectId,
        column.createdAt,
        column.updatedAt,
        column.slug,
        column.userId
      ).values(
        title,
        content,
        projectId,
        createdAt,
        updatedAt,
        slug,
        userId
      )
    }.updateAndReturnGeneratedKey.apply()

    Wikis(
      id = generatedKey.toInt,
      title = title,
      content = content,
      projectId = projectId,
      createdAt = createdAt,
      updatedAt = updatedAt,
      slug = slug,
      userId = userId)
  }

  def save(entity: Wikis)(implicit session: DBSession = autoSession): Wikis = {
    withSQL {
      update(Wikis).set(
        column.id -> entity.id,
        column.title -> entity.title,
        column.content -> entity.content,
        column.projectId -> entity.projectId,
        column.createdAt -> entity.createdAt,
        column.updatedAt -> entity.updatedAt,
        column.slug -> entity.slug,
        column.userId -> entity.userId
      ).where.eq(column.id, entity.id)
    }.update.apply()
    entity
  }

  def destroy(entity: Wikis)(implicit session: DBSession = autoSession): Unit = {
    withSQL { delete.from(Wikis).where.eq(column.id, entity.id) }.update.apply()
  }

}
