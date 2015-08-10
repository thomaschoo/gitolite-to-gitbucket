package models.gitbucket

import scalikejdbc._

case class WebHook(
  userName: String,
  repositoryName: String,
  url: String) {

  def save()(implicit session: DBSession = WebHook.autoSession): WebHook = WebHook.save(this)(session)

  def destroy()(implicit session: DBSession = WebHook.autoSession): Unit = WebHook.destroy(this)(session)

}


object WebHook extends SQLSyntaxSupport[WebHook] {

  override val tableName = "WEB_HOOK"

  override val columns = Seq("USER_NAME", "REPOSITORY_NAME", "URL")

  def apply(wh: SyntaxProvider[WebHook])(rs: WrappedResultSet): WebHook = apply(wh.resultName)(rs)
  def apply(wh: ResultName[WebHook])(rs: WrappedResultSet): WebHook = new WebHook(
    userName = rs.get(wh.userName),
    repositoryName = rs.get(wh.repositoryName),
    url = rs.get(wh.url)
  )

  val wh = WebHook.syntax("wh")

  override val autoSession = AutoSession

  def find(repositoryName: String, url: String, userName: String)(implicit session: DBSession = autoSession): Option[WebHook] = {
    withSQL {
      select.from(WebHook as wh).where.eq(wh.repositoryName, repositoryName).and.eq(wh.url, url).and.eq(wh.userName, userName)
    }.map(WebHook(wh.resultName)).single.apply()
  }

  def findAll()(implicit session: DBSession = autoSession): List[WebHook] = {
    withSQL(select.from(WebHook as wh)).map(WebHook(wh.resultName)).list.apply()
  }

  def countAll()(implicit session: DBSession = autoSession): Long = {
    withSQL(select(sqls.count).from(WebHook as wh)).map(rs => rs.long(1)).single.apply().get
  }

  def findBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Option[WebHook] = {
    withSQL {
      select.from(WebHook as wh).where.append(where)
    }.map(WebHook(wh.resultName)).single.apply()
  }

  def findAllBy(where: SQLSyntax)(implicit session: DBSession = autoSession): List[WebHook] = {
    withSQL {
      select.from(WebHook as wh).where.append(where)
    }.map(WebHook(wh.resultName)).list.apply()
  }

  def countBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Long = {
    withSQL {
      select(sqls.count).from(WebHook as wh).where.append(where)
    }.map(_.long(1)).single.apply().get
  }

  def create(
    userName: String,
    repositoryName: String,
    url: String)(implicit session: DBSession = autoSession): WebHook = {
    withSQL {
      insert.into(WebHook).columns(
        column.userName,
        column.repositoryName,
        column.url
      ).values(
        userName,
        repositoryName,
        url
      )
    }.update.apply()

    WebHook(
      userName = userName,
      repositoryName = repositoryName,
      url = url)
  }

  def save(entity: WebHook)(implicit session: DBSession = autoSession): WebHook = {
    withSQL {
      update(WebHook).set(
        column.userName -> entity.userName,
        column.repositoryName -> entity.repositoryName,
        column.url -> entity.url
      ).where.eq(column.repositoryName, entity.repositoryName).and.eq(column.url, entity.url).and.eq(column.userName, entity.userName)
    }.update.apply()
    entity
  }

  def destroy(entity: WebHook)(implicit session: DBSession = autoSession): Unit = {
    withSQL { delete.from(WebHook).where.eq(column.repositoryName, entity.repositoryName).and.eq(column.url, entity.url).and.eq(column.userName, entity.userName) }.update.apply()
  }

}
