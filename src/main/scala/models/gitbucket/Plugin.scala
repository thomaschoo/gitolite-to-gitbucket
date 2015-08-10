package models.gitbucket

import scalikejdbc._

case class Plugin(
  pluginId: String,
  version: String) {

  def save()(implicit session: DBSession = Plugin.autoSession): Plugin = Plugin.save(this)(session)

  def destroy()(implicit session: DBSession = Plugin.autoSession): Unit = Plugin.destroy(this)(session)

}


object Plugin extends SQLSyntaxSupport[Plugin] {

  override val tableName = "PLUGIN"

  override val columns = Seq("PLUGIN_ID", "VERSION")

  def apply(p: SyntaxProvider[Plugin])(rs: WrappedResultSet): Plugin = apply(p.resultName)(rs)
  def apply(p: ResultName[Plugin])(rs: WrappedResultSet): Plugin = new Plugin(
    pluginId = rs.get(p.pluginId),
    version = rs.get(p.version)
  )

  val p = Plugin.syntax("p")

  override val autoSession = AutoSession

  def find(pluginId: String)(implicit session: DBSession = autoSession): Option[Plugin] = {
    withSQL {
      select.from(Plugin as p).where.eq(p.pluginId, pluginId)
    }.map(Plugin(p.resultName)).single.apply()
  }

  def findAll()(implicit session: DBSession = autoSession): List[Plugin] = {
    withSQL(select.from(Plugin as p)).map(Plugin(p.resultName)).list.apply()
  }

  def countAll()(implicit session: DBSession = autoSession): Long = {
    withSQL(select(sqls.count).from(Plugin as p)).map(rs => rs.long(1)).single.apply().get
  }

  def findBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Option[Plugin] = {
    withSQL {
      select.from(Plugin as p).where.append(where)
    }.map(Plugin(p.resultName)).single.apply()
  }

  def findAllBy(where: SQLSyntax)(implicit session: DBSession = autoSession): List[Plugin] = {
    withSQL {
      select.from(Plugin as p).where.append(where)
    }.map(Plugin(p.resultName)).list.apply()
  }

  def countBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Long = {
    withSQL {
      select(sqls.count).from(Plugin as p).where.append(where)
    }.map(_.long(1)).single.apply().get
  }

  def create(
    pluginId: String,
    version: String)(implicit session: DBSession = autoSession): Plugin = {
    withSQL {
      insert.into(Plugin).columns(
        column.pluginId,
        column.version
      ).values(
        pluginId,
        version
      )
    }.update.apply()

    Plugin(
      pluginId = pluginId,
      version = version)
  }

  def save(entity: Plugin)(implicit session: DBSession = autoSession): Plugin = {
    withSQL {
      update(Plugin).set(
        column.pluginId -> entity.pluginId,
        column.version -> entity.version
      ).where.eq(column.pluginId, entity.pluginId)
    }.update.apply()
    entity
  }

  def destroy(entity: Plugin)(implicit session: DBSession = autoSession): Unit = {
    withSQL { delete.from(Plugin).where.eq(column.pluginId, entity.pluginId) }.update.apply()
  }

}
