package models.gitolite

import org.joda.time.DateTime

import scalikejdbc._

case class ProtectedBranches(
  id: Int,
  projectId: Int,
  name: String,
  createdAt: DateTime,
  updatedAt: DateTime) {

  def save()(implicit session: DBSession = ProtectedBranches.autoSession): ProtectedBranches = ProtectedBranches.save(this)(session)

  def destroy()(implicit session: DBSession = ProtectedBranches.autoSession): Unit = ProtectedBranches.destroy(this)(session)

}


object ProtectedBranches extends SQLSyntaxSupport[ProtectedBranches] {

  override val tableName = "protected_branches"

  override val columns = Seq("id", "project_id", "name", "created_at", "updated_at")

  def apply(pb: SyntaxProvider[ProtectedBranches])(rs: WrappedResultSet): ProtectedBranches = apply(pb.resultName)(rs)
  def apply(pb: ResultName[ProtectedBranches])(rs: WrappedResultSet): ProtectedBranches = new ProtectedBranches(
    id = rs.get(pb.id),
    projectId = rs.get(pb.projectId),
    name = rs.get(pb.name),
    createdAt = rs.get(pb.createdAt),
    updatedAt = rs.get(pb.updatedAt)
  )

  val pb = ProtectedBranches.syntax("pb")

  override val autoSession = AutoSession

  def find(id: Int)(implicit session: DBSession = autoSession): Option[ProtectedBranches] = {
    withSQL {
      select.from(ProtectedBranches as pb).where.eq(pb.id, id)
    }.map(ProtectedBranches(pb.resultName)).single.apply()
  }

  def findAll()(implicit session: DBSession = autoSession): List[ProtectedBranches] = {
    withSQL(select.from(ProtectedBranches as pb)).map(ProtectedBranches(pb.resultName)).list.apply()
  }

  def countAll()(implicit session: DBSession = autoSession): Long = {
    withSQL(select(sqls.count).from(ProtectedBranches as pb)).map(rs => rs.long(1)).single.apply().get
  }

  def findBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Option[ProtectedBranches] = {
    withSQL {
      select.from(ProtectedBranches as pb).where.append(where)
    }.map(ProtectedBranches(pb.resultName)).single.apply()
  }

  def findAllBy(where: SQLSyntax)(implicit session: DBSession = autoSession): List[ProtectedBranches] = {
    withSQL {
      select.from(ProtectedBranches as pb).where.append(where)
    }.map(ProtectedBranches(pb.resultName)).list.apply()
  }

  def countBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Long = {
    withSQL {
      select(sqls.count).from(ProtectedBranches as pb).where.append(where)
    }.map(_.long(1)).single.apply().get
  }

  def create(
    projectId: Int,
    name: String,
    createdAt: DateTime,
    updatedAt: DateTime)(implicit session: DBSession = autoSession): ProtectedBranches = {
    val generatedKey = withSQL {
      insert.into(ProtectedBranches).columns(
        column.projectId,
        column.name,
        column.createdAt,
        column.updatedAt
      ).values(
        projectId,
        name,
        createdAt,
        updatedAt
      )
    }.updateAndReturnGeneratedKey.apply()

    ProtectedBranches(
      id = generatedKey.toInt,
      projectId = projectId,
      name = name,
      createdAt = createdAt,
      updatedAt = updatedAt)
  }

  def save(entity: ProtectedBranches)(implicit session: DBSession = autoSession): ProtectedBranches = {
    withSQL {
      update(ProtectedBranches).set(
        column.id -> entity.id,
        column.projectId -> entity.projectId,
        column.name -> entity.name,
        column.createdAt -> entity.createdAt,
        column.updatedAt -> entity.updatedAt
      ).where.eq(column.id, entity.id)
    }.update.apply()
    entity
  }

  def destroy(entity: ProtectedBranches)(implicit session: DBSession = autoSession): Unit = {
    withSQL { delete.from(ProtectedBranches).where.eq(column.id, entity.id) }.update.apply()
  }

}
