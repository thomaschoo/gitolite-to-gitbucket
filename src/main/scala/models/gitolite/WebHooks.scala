package models.gitolite

import org.joda.time.DateTime

import scalikejdbc._

case class WebHooks(
  id: Int,
  url: Option[String] = None,
  projectId: Option[Int] = None,
  createdAt: DateTime,
  updatedAt: DateTime) {

  def save()(implicit session: DBSession = WebHooks.autoSession): WebHooks = WebHooks.save(this)(session)

  def destroy()(implicit session: DBSession = WebHooks.autoSession): Unit = WebHooks.destroy(this)(session)

}


object WebHooks extends SQLSyntaxSupport[WebHooks] {

  override val tableName = "web_hooks"

  override val columns = Seq("id", "url", "project_id", "created_at", "updated_at")

  def apply(wh: SyntaxProvider[WebHooks])(rs: WrappedResultSet): WebHooks = apply(wh.resultName)(rs)
  def apply(wh: ResultName[WebHooks])(rs: WrappedResultSet): WebHooks = new WebHooks(
    id = rs.get(wh.id),
    url = rs.get(wh.url),
    projectId = rs.get(wh.projectId),
    createdAt = rs.get(wh.createdAt),
    updatedAt = rs.get(wh.updatedAt)
  )

  val wh = WebHooks.syntax("wh")

  override val autoSession = AutoSession

  def find(id: Int)(implicit session: DBSession = autoSession): Option[WebHooks] = {
    withSQL {
      select.from(WebHooks as wh).where.eq(wh.id, id)
    }.map(WebHooks(wh.resultName)).single.apply()
  }

  def findAll()(implicit session: DBSession = autoSession): List[WebHooks] = {
    withSQL(select.from(WebHooks as wh)).map(WebHooks(wh.resultName)).list.apply()
  }

  def countAll()(implicit session: DBSession = autoSession): Long = {
    withSQL(select(sqls.count).from(WebHooks as wh)).map(rs => rs.long(1)).single.apply().get
  }

  def findBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Option[WebHooks] = {
    withSQL {
      select.from(WebHooks as wh).where.append(where)
    }.map(WebHooks(wh.resultName)).single.apply()
  }

  def findAllBy(where: SQLSyntax)(implicit session: DBSession = autoSession): List[WebHooks] = {
    withSQL {
      select.from(WebHooks as wh).where.append(where)
    }.map(WebHooks(wh.resultName)).list.apply()
  }

  def countBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Long = {
    withSQL {
      select(sqls.count).from(WebHooks as wh).where.append(where)
    }.map(_.long(1)).single.apply().get
  }

  def create(
    url: Option[String] = None,
    projectId: Option[Int] = None,
    createdAt: DateTime,
    updatedAt: DateTime)(implicit session: DBSession = autoSession): WebHooks = {
    val generatedKey = withSQL {
      insert.into(WebHooks).columns(
        column.url,
        column.projectId,
        column.createdAt,
        column.updatedAt
      ).values(
        url,
        projectId,
        createdAt,
        updatedAt
      )
    }.updateAndReturnGeneratedKey.apply()

    WebHooks(
      id = generatedKey.toInt,
      url = url,
      projectId = projectId,
      createdAt = createdAt,
      updatedAt = updatedAt)
  }

  def save(entity: WebHooks)(implicit session: DBSession = autoSession): WebHooks = {
    withSQL {
      update(WebHooks).set(
        column.id -> entity.id,
        column.url -> entity.url,
        column.projectId -> entity.projectId,
        column.createdAt -> entity.createdAt,
        column.updatedAt -> entity.updatedAt
      ).where.eq(column.id, entity.id)
    }.update.apply()
    entity
  }

  def destroy(entity: WebHooks)(implicit session: DBSession = autoSession): Unit = {
    withSQL { delete.from(WebHooks).where.eq(column.id, entity.id) }.update.apply()
  }

}
