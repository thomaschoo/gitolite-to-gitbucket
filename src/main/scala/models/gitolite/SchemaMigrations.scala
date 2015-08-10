package models.gitolite

import scalikejdbc._

case class SchemaMigrations(
  version: String) {

  def save()(implicit session: DBSession = SchemaMigrations.autoSession): SchemaMigrations = SchemaMigrations.save(this)(session)

  def destroy()(implicit session: DBSession = SchemaMigrations.autoSession): Unit = SchemaMigrations.destroy(this)(session)

}


object SchemaMigrations extends SQLSyntaxSupport[SchemaMigrations] {

  override val tableName = "schema_migrations"

  override val columns = Seq("version")

  def apply(sm: SyntaxProvider[SchemaMigrations])(rs: WrappedResultSet): SchemaMigrations = apply(sm.resultName)(rs)
  def apply(sm: ResultName[SchemaMigrations])(rs: WrappedResultSet): SchemaMigrations = new SchemaMigrations(
    version = rs.get(sm.version)
  )

  val sm = SchemaMigrations.syntax("sm")

  override val autoSession = AutoSession

  def find(version: String)(implicit session: DBSession = autoSession): Option[SchemaMigrations] = {
    withSQL {
      select.from(SchemaMigrations as sm).where.eq(sm.version, version)
    }.map(SchemaMigrations(sm.resultName)).single.apply()
  }

  def findAll()(implicit session: DBSession = autoSession): List[SchemaMigrations] = {
    withSQL(select.from(SchemaMigrations as sm)).map(SchemaMigrations(sm.resultName)).list.apply()
  }

  def countAll()(implicit session: DBSession = autoSession): Long = {
    withSQL(select(sqls.count).from(SchemaMigrations as sm)).map(rs => rs.long(1)).single.apply().get
  }

  def findBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Option[SchemaMigrations] = {
    withSQL {
      select.from(SchemaMigrations as sm).where.append(where)
    }.map(SchemaMigrations(sm.resultName)).single.apply()
  }

  def findAllBy(where: SQLSyntax)(implicit session: DBSession = autoSession): List[SchemaMigrations] = {
    withSQL {
      select.from(SchemaMigrations as sm).where.append(where)
    }.map(SchemaMigrations(sm.resultName)).list.apply()
  }

  def countBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Long = {
    withSQL {
      select(sqls.count).from(SchemaMigrations as sm).where.append(where)
    }.map(_.long(1)).single.apply().get
  }

  def create(
    version: String)(implicit session: DBSession = autoSession): SchemaMigrations = {
    withSQL {
      insert.into(SchemaMigrations).columns(
        column.version
      ).values(
        version
      )
    }.update.apply()

    SchemaMigrations(
      version = version)
  }

  def save(entity: SchemaMigrations)(implicit session: DBSession = autoSession): SchemaMigrations = {
    withSQL {
      update(SchemaMigrations).set(
        column.version -> entity.version
      ).where.eq(column.version, entity.version)
    }.update.apply()
    entity
  }

  def destroy(entity: SchemaMigrations)(implicit session: DBSession = autoSession): Unit = {
    withSQL { delete.from(SchemaMigrations).where.eq(column.version, entity.version) }.update.apply()
  }

}
