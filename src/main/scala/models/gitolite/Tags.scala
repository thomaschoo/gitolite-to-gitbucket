package models.gitolite

import scalikejdbc._

case class Tags(
  id: Int,
  name: Option[String] = None) {

  def save()(implicit session: DBSession = Tags.autoSession): Tags = Tags.save(this)(session)

  def destroy()(implicit session: DBSession = Tags.autoSession): Unit = Tags.destroy(this)(session)

}


object Tags extends SQLSyntaxSupport[Tags] {

  override val tableName = "tags"

  override val columns = Seq("id", "name")

  def apply(t: SyntaxProvider[Tags])(rs: WrappedResultSet): Tags = apply(t.resultName)(rs)
  def apply(t: ResultName[Tags])(rs: WrappedResultSet): Tags = new Tags(
    id = rs.get(t.id),
    name = rs.get(t.name)
  )

  val t = Tags.syntax("t")

  override val autoSession = AutoSession

  def find(id: Int)(implicit session: DBSession = autoSession): Option[Tags] = {
    withSQL {
      select.from(Tags as t).where.eq(t.id, id)
    }.map(Tags(t.resultName)).single.apply()
  }

  def findAll()(implicit session: DBSession = autoSession): List[Tags] = {
    withSQL(select.from(Tags as t)).map(Tags(t.resultName)).list.apply()
  }

  def countAll()(implicit session: DBSession = autoSession): Long = {
    withSQL(select(sqls.count).from(Tags as t)).map(rs => rs.long(1)).single.apply().get
  }

  def findBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Option[Tags] = {
    withSQL {
      select.from(Tags as t).where.append(where)
    }.map(Tags(t.resultName)).single.apply()
  }

  def findAllBy(where: SQLSyntax)(implicit session: DBSession = autoSession): List[Tags] = {
    withSQL {
      select.from(Tags as t).where.append(where)
    }.map(Tags(t.resultName)).list.apply()
  }

  def countBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Long = {
    withSQL {
      select(sqls.count).from(Tags as t).where.append(where)
    }.map(_.long(1)).single.apply().get
  }

  def create(
    name: Option[String] = None)(implicit session: DBSession = autoSession): Tags = {
    val generatedKey = withSQL {
      insert.into(Tags).columns(
        column.name
      ).values(
        name
      )
    }.updateAndReturnGeneratedKey.apply()

    Tags(
      id = generatedKey.toInt,
      name = name)
  }

  def save(entity: Tags)(implicit session: DBSession = autoSession): Tags = {
    withSQL {
      update(Tags).set(
        column.id -> entity.id,
        column.name -> entity.name
      ).where.eq(column.id, entity.id)
    }.update.apply()
    entity
  }

  def destroy(entity: Tags)(implicit session: DBSession = autoSession): Unit = {
    withSQL { delete.from(Tags).where.eq(column.id, entity.id) }.update.apply()
  }

}
