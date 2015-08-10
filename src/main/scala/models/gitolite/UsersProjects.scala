package models.gitolite

import org.joda.time.DateTime

import scalikejdbc._

case class UsersProjects(
  id: Int,
  userId: Int,
  projectId: Int,
  createdAt: DateTime,
  updatedAt: DateTime,
  projectAccess: Int,
  generatedForPublicAccess: Option[Boolean] = None) {

  def save()(implicit session: DBSession = UsersProjects.autoSession): UsersProjects = UsersProjects.save(this)(session)

  def destroy()(implicit session: DBSession = UsersProjects.autoSession): Unit = UsersProjects.destroy(this)(session)

}


object UsersProjects extends SQLSyntaxSupport[UsersProjects] {

  override val tableName = "users_projects"

  override val columns = Seq("id", "user_id", "project_id", "created_at", "updated_at", "project_access", "generated_for_public_access")

  def apply(up: SyntaxProvider[UsersProjects])(rs: WrappedResultSet): UsersProjects = apply(up.resultName)(rs)
  def apply(up: ResultName[UsersProjects])(rs: WrappedResultSet): UsersProjects = new UsersProjects(
    id = rs.get(up.id),
    userId = rs.get(up.userId),
    projectId = rs.get(up.projectId),
    createdAt = rs.get(up.createdAt),
    updatedAt = rs.get(up.updatedAt),
    projectAccess = rs.get(up.projectAccess),
    generatedForPublicAccess = rs.get(up.generatedForPublicAccess)
  )

  val up = UsersProjects.syntax("up")

  override val autoSession = AutoSession

  def find(id: Int)(implicit session: DBSession = autoSession): Option[UsersProjects] = {
    withSQL {
      select.from(UsersProjects as up).where.eq(up.id, id)
    }.map(UsersProjects(up.resultName)).single.apply()
  }

  def findAll()(implicit session: DBSession = autoSession): List[UsersProjects] = {
    withSQL(select.from(UsersProjects as up)).map(UsersProjects(up.resultName)).list.apply()
  }

  def countAll()(implicit session: DBSession = autoSession): Long = {
    withSQL(select(sqls.count).from(UsersProjects as up)).map(rs => rs.long(1)).single.apply().get
  }

  def findBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Option[UsersProjects] = {
    withSQL {
      select.from(UsersProjects as up).where.append(where)
    }.map(UsersProjects(up.resultName)).single.apply()
  }

  def findAllBy(where: SQLSyntax)(implicit session: DBSession = autoSession): List[UsersProjects] = {
    withSQL {
      select.from(UsersProjects as up).where.append(where)
    }.map(UsersProjects(up.resultName)).list.apply()
  }

  def countBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Long = {
    withSQL {
      select(sqls.count).from(UsersProjects as up).where.append(where)
    }.map(_.long(1)).single.apply().get
  }

  def create(
    userId: Int,
    projectId: Int,
    createdAt: DateTime,
    updatedAt: DateTime,
    projectAccess: Int,
    generatedForPublicAccess: Option[Boolean] = None)(implicit session: DBSession = autoSession): UsersProjects = {
    val generatedKey = withSQL {
      insert.into(UsersProjects).columns(
        column.userId,
        column.projectId,
        column.createdAt,
        column.updatedAt,
        column.projectAccess,
        column.generatedForPublicAccess
      ).values(
        userId,
        projectId,
        createdAt,
        updatedAt,
        projectAccess,
        generatedForPublicAccess
      )
    }.updateAndReturnGeneratedKey.apply()

    UsersProjects(
      id = generatedKey.toInt,
      userId = userId,
      projectId = projectId,
      createdAt = createdAt,
      updatedAt = updatedAt,
      projectAccess = projectAccess,
      generatedForPublicAccess = generatedForPublicAccess)
  }

  def save(entity: UsersProjects)(implicit session: DBSession = autoSession): UsersProjects = {
    withSQL {
      update(UsersProjects).set(
        column.id -> entity.id,
        column.userId -> entity.userId,
        column.projectId -> entity.projectId,
        column.createdAt -> entity.createdAt,
        column.updatedAt -> entity.updatedAt,
        column.projectAccess -> entity.projectAccess,
        column.generatedForPublicAccess -> entity.generatedForPublicAccess
      ).where.eq(column.id, entity.id)
    }.update.apply()
    entity
  }

  def destroy(entity: UsersProjects)(implicit session: DBSession = autoSession): Unit = {
    withSQL { delete.from(UsersProjects).where.eq(column.id, entity.id) }.update.apply()
  }

}
