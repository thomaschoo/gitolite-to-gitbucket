package models.gitolite

import org.joda.time.DateTime

import scalikejdbc._

case class Keys(
  id: Int,
  userId: Option[Int] = None,
  createdAt: DateTime,
  updatedAt: DateTime,
  key: Option[String] = None,
  title: Option[String] = None,
  identifier: Option[String] = None,
  projectId: Option[Int] = None,
  privateKey: Option[String] = None,
  puttyKey: Option[String] = None) {

  def save()(implicit session: DBSession = Keys.autoSession): Keys = Keys.save(this)(session)

  def destroy()(implicit session: DBSession = Keys.autoSession): Unit = Keys.destroy(this)(session)

}


object Keys extends SQLSyntaxSupport[Keys] {

  override val tableName = "keys"

  override val columns = Seq("id", "user_id", "created_at", "updated_at", "key", "title", "identifier", "project_id", "private_key", "putty_key")

  def apply(k: SyntaxProvider[Keys])(rs: WrappedResultSet): Keys = apply(k.resultName)(rs)
  def apply(k: ResultName[Keys])(rs: WrappedResultSet): Keys = new Keys(
    id = rs.get(k.id),
    userId = rs.get(k.userId),
    createdAt = rs.get(k.createdAt),
    updatedAt = rs.get(k.updatedAt),
    key = rs.get(k.key),
    title = rs.get(k.title),
    identifier = rs.get(k.identifier),
    projectId = rs.get(k.projectId),
    privateKey = rs.get(k.privateKey),
    puttyKey = rs.get(k.puttyKey)
  )

  val k = Keys.syntax("k")

  override val autoSession = AutoSession

  def find(id: Int)(implicit session: DBSession = autoSession): Option[Keys] = {
    withSQL {
      select.from(Keys as k).where.eq(k.id, id)
    }.map(Keys(k.resultName)).single.apply()
  }

  def findAll()(implicit session: DBSession = autoSession): List[Keys] = {
    withSQL(select.from(Keys as k)).map(Keys(k.resultName)).list.apply()
  }

  def countAll()(implicit session: DBSession = autoSession): Long = {
    withSQL(select(sqls.count).from(Keys as k)).map(rs => rs.long(1)).single.apply().get
  }

  def findBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Option[Keys] = {
    withSQL {
      select.from(Keys as k).where.append(where)
    }.map(Keys(k.resultName)).single.apply()
  }

  def findAllBy(where: SQLSyntax)(implicit session: DBSession = autoSession): List[Keys] = {
    withSQL {
      select.from(Keys as k).where.append(where)
    }.map(Keys(k.resultName)).list.apply()
  }

  def countBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Long = {
    withSQL {
      select(sqls.count).from(Keys as k).where.append(where)
    }.map(_.long(1)).single.apply().get
  }

  def create(
    userId: Option[Int] = None,
    createdAt: DateTime,
    updatedAt: DateTime,
    key: Option[String] = None,
    title: Option[String] = None,
    identifier: Option[String] = None,
    projectId: Option[Int] = None,
    privateKey: Option[String] = None,
    puttyKey: Option[String] = None)(implicit session: DBSession = autoSession): Keys = {
    val generatedKey = withSQL {
      insert.into(Keys).columns(
        column.userId,
        column.createdAt,
        column.updatedAt,
        column.key,
        column.title,
        column.identifier,
        column.projectId,
        column.privateKey,
        column.puttyKey
      ).values(
        userId,
        createdAt,
        updatedAt,
        key,
        title,
        identifier,
        projectId,
        privateKey,
        puttyKey
      )
    }.updateAndReturnGeneratedKey.apply()

    Keys(
      id = generatedKey.toInt,
      userId = userId,
      createdAt = createdAt,
      updatedAt = updatedAt,
      key = key,
      title = title,
      identifier = identifier,
      projectId = projectId,
      privateKey = privateKey,
      puttyKey = puttyKey)
  }

  def save(entity: Keys)(implicit session: DBSession = autoSession): Keys = {
    withSQL {
      update(Keys).set(
        column.id -> entity.id,
        column.userId -> entity.userId,
        column.createdAt -> entity.createdAt,
        column.updatedAt -> entity.updatedAt,
        column.key -> entity.key,
        column.title -> entity.title,
        column.identifier -> entity.identifier,
        column.projectId -> entity.projectId,
        column.privateKey -> entity.privateKey,
        column.puttyKey -> entity.puttyKey
      ).where.eq(column.id, entity.id)
    }.update.apply()
    entity
  }

  def destroy(entity: Keys)(implicit session: DBSession = autoSession): Unit = {
    withSQL { delete.from(Keys).where.eq(column.id, entity.id) }.update.apply()
  }

}
