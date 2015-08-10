package models.gitolite

import org.joda.time.DateTime

import scalikejdbc._

case class Taggings(
  id: Int,
  tagId: Option[Int] = None,
  taggableId: Option[Int] = None,
  taggableType: Option[String] = None,
  taggerId: Option[Int] = None,
  taggerType: Option[String] = None,
  context: Option[String] = None,
  createdAt: Option[DateTime] = None) {

  def save()(implicit session: DBSession = Taggings.autoSession): Taggings = Taggings.save(this)(session)

  def destroy()(implicit session: DBSession = Taggings.autoSession): Unit = Taggings.destroy(this)(session)

}


object Taggings extends SQLSyntaxSupport[Taggings] {

  override val tableName = "taggings"

  override val columns = Seq("id", "tag_id", "taggable_id", "taggable_type", "tagger_id", "tagger_type", "context", "created_at")

  def apply(t: SyntaxProvider[Taggings])(rs: WrappedResultSet): Taggings = apply(t.resultName)(rs)
  def apply(t: ResultName[Taggings])(rs: WrappedResultSet): Taggings = new Taggings(
    id = rs.get(t.id),
    tagId = rs.get(t.tagId),
    taggableId = rs.get(t.taggableId),
    taggableType = rs.get(t.taggableType),
    taggerId = rs.get(t.taggerId),
    taggerType = rs.get(t.taggerType),
    context = rs.get(t.context),
    createdAt = rs.get(t.createdAt)
  )

  val t = Taggings.syntax("t")

  override val autoSession = AutoSession

  def find(id: Int)(implicit session: DBSession = autoSession): Option[Taggings] = {
    withSQL {
      select.from(Taggings as t).where.eq(t.id, id)
    }.map(Taggings(t.resultName)).single.apply()
  }

  def findAll()(implicit session: DBSession = autoSession): List[Taggings] = {
    withSQL(select.from(Taggings as t)).map(Taggings(t.resultName)).list.apply()
  }

  def countAll()(implicit session: DBSession = autoSession): Long = {
    withSQL(select(sqls.count).from(Taggings as t)).map(rs => rs.long(1)).single.apply().get
  }

  def findBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Option[Taggings] = {
    withSQL {
      select.from(Taggings as t).where.append(where)
    }.map(Taggings(t.resultName)).single.apply()
  }

  def findAllBy(where: SQLSyntax)(implicit session: DBSession = autoSession): List[Taggings] = {
    withSQL {
      select.from(Taggings as t).where.append(where)
    }.map(Taggings(t.resultName)).list.apply()
  }

  def countBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Long = {
    withSQL {
      select(sqls.count).from(Taggings as t).where.append(where)
    }.map(_.long(1)).single.apply().get
  }

  def create(
    tagId: Option[Int] = None,
    taggableId: Option[Int] = None,
    taggableType: Option[String] = None,
    taggerId: Option[Int] = None,
    taggerType: Option[String] = None,
    context: Option[String] = None,
    createdAt: Option[DateTime] = None)(implicit session: DBSession = autoSession): Taggings = {
    val generatedKey = withSQL {
      insert.into(Taggings).columns(
        column.tagId,
        column.taggableId,
        column.taggableType,
        column.taggerId,
        column.taggerType,
        column.context,
        column.createdAt
      ).values(
        tagId,
        taggableId,
        taggableType,
        taggerId,
        taggerType,
        context,
        createdAt
      )
    }.updateAndReturnGeneratedKey.apply()

    Taggings(
      id = generatedKey.toInt,
      tagId = tagId,
      taggableId = taggableId,
      taggableType = taggableType,
      taggerId = taggerId,
      taggerType = taggerType,
      context = context,
      createdAt = createdAt)
  }

  def save(entity: Taggings)(implicit session: DBSession = autoSession): Taggings = {
    withSQL {
      update(Taggings).set(
        column.id -> entity.id,
        column.tagId -> entity.tagId,
        column.taggableId -> entity.taggableId,
        column.taggableType -> entity.taggableType,
        column.taggerId -> entity.taggerId,
        column.taggerType -> entity.taggerType,
        column.context -> entity.context,
        column.createdAt -> entity.createdAt
      ).where.eq(column.id, entity.id)
    }.update.apply()
    entity
  }

  def destroy(entity: Taggings)(implicit session: DBSession = autoSession): Unit = {
    withSQL { delete.from(Taggings).where.eq(column.id, entity.id) }.update.apply()
  }

}
