package models.gitolite

import org.joda.time.DateTime

import scalikejdbc._

case class Projects(
  id: Int,
  name: Option[String] = None,
  path: Option[String] = None,
  description: Option[String] = None,
  createdAt: DateTime,
  updatedAt: DateTime,
  privateFlag: Boolean,
  code: Option[String] = None,
  ownerId: Option[Int] = None,
  defaultBranch: String,
  issuesEnabled: Boolean,
  wallEnabled: Boolean,
  mergeRequestsEnabled: Boolean,
  wikiEnabled: Boolean) {

  def save()(implicit session: DBSession = Projects.autoSession): Projects = Projects.save(this)(session)

  def destroy()(implicit session: DBSession = Projects.autoSession): Unit = Projects.destroy(this)(session)

}


object Projects extends SQLSyntaxSupport[Projects] {

  override val tableName = "projects"

  override val columns = Seq("id", "name", "path", "description", "created_at", "updated_at", "private_flag", "code", "owner_id", "default_branch", "issues_enabled", "wall_enabled", "merge_requests_enabled", "wiki_enabled")

  def apply(p: SyntaxProvider[Projects])(rs: WrappedResultSet): Projects = apply(p.resultName)(rs)
  def apply(p: ResultName[Projects])(rs: WrappedResultSet): Projects = new Projects(
    id = rs.get(p.id),
    name = rs.get(p.name),
    path = rs.get(p.path),
    description = rs.get(p.description),
    createdAt = rs.get(p.createdAt),
    updatedAt = rs.get(p.updatedAt),
    privateFlag = rs.get(p.privateFlag),
    code = rs.get(p.code),
    ownerId = rs.get(p.ownerId),
    defaultBranch = rs.get(p.defaultBranch),
    issuesEnabled = rs.get(p.issuesEnabled),
    wallEnabled = rs.get(p.wallEnabled),
    mergeRequestsEnabled = rs.get(p.mergeRequestsEnabled),
    wikiEnabled = rs.get(p.wikiEnabled)
  )

  val p = Projects.syntax("p")

  override val autoSession = AutoSession

  def find(id: Int)(implicit session: DBSession = autoSession): Option[Projects] = {
    withSQL {
      select.from(Projects as p).where.eq(p.id, id)
    }.map(Projects(p.resultName)).single.apply()
  }

  def findAll()(implicit session: DBSession = autoSession): List[Projects] = {
    withSQL(select.from(Projects as p)).map(Projects(p.resultName)).list.apply()
  }

  def countAll()(implicit session: DBSession = autoSession): Long = {
    withSQL(select(sqls.count).from(Projects as p)).map(rs => rs.long(1)).single.apply().get
  }

  def findBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Option[Projects] = {
    withSQL {
      select.from(Projects as p).where.append(where)
    }.map(Projects(p.resultName)).single.apply()
  }

  def findAllBy(where: SQLSyntax)(implicit session: DBSession = autoSession): List[Projects] = {
    withSQL {
      select.from(Projects as p).where.append(where)
    }.map(Projects(p.resultName)).list.apply()
  }

  def countBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Long = {
    withSQL {
      select(sqls.count).from(Projects as p).where.append(where)
    }.map(_.long(1)).single.apply().get
  }

  def create(
    name: Option[String] = None,
    path: Option[String] = None,
    description: Option[String] = None,
    createdAt: DateTime,
    updatedAt: DateTime,
    privateFlag: Boolean,
    code: Option[String] = None,
    ownerId: Option[Int] = None,
    defaultBranch: String,
    issuesEnabled: Boolean,
    wallEnabled: Boolean,
    mergeRequestsEnabled: Boolean,
    wikiEnabled: Boolean)(implicit session: DBSession = autoSession): Projects = {
    val generatedKey = withSQL {
      insert.into(Projects).columns(
        column.name,
        column.path,
        column.description,
        column.createdAt,
        column.updatedAt,
        column.privateFlag,
        column.code,
        column.ownerId,
        column.defaultBranch,
        column.issuesEnabled,
        column.wallEnabled,
        column.mergeRequestsEnabled,
        column.wikiEnabled
      ).values(
        name,
        path,
        description,
        createdAt,
        updatedAt,
        privateFlag,
        code,
        ownerId,
        defaultBranch,
        issuesEnabled,
        wallEnabled,
        mergeRequestsEnabled,
        wikiEnabled
      )
    }.updateAndReturnGeneratedKey.apply()

    Projects(
      id = generatedKey.toInt,
      name = name,
      path = path,
      description = description,
      createdAt = createdAt,
      updatedAt = updatedAt,
      privateFlag = privateFlag,
      code = code,
      ownerId = ownerId,
      defaultBranch = defaultBranch,
      issuesEnabled = issuesEnabled,
      wallEnabled = wallEnabled,
      mergeRequestsEnabled = mergeRequestsEnabled,
      wikiEnabled = wikiEnabled)
  }

  def save(entity: Projects)(implicit session: DBSession = autoSession): Projects = {
    withSQL {
      update(Projects).set(
        column.id -> entity.id,
        column.name -> entity.name,
        column.path -> entity.path,
        column.description -> entity.description,
        column.createdAt -> entity.createdAt,
        column.updatedAt -> entity.updatedAt,
        column.privateFlag -> entity.privateFlag,
        column.code -> entity.code,
        column.ownerId -> entity.ownerId,
        column.defaultBranch -> entity.defaultBranch,
        column.issuesEnabled -> entity.issuesEnabled,
        column.wallEnabled -> entity.wallEnabled,
        column.mergeRequestsEnabled -> entity.mergeRequestsEnabled,
        column.wikiEnabled -> entity.wikiEnabled
      ).where.eq(column.id, entity.id)
    }.update.apply()
    entity
  }

  def destroy(entity: Projects)(implicit session: DBSession = autoSession): Unit = {
    withSQL { delete.from(Projects).where.eq(column.id, entity.id) }.update.apply()
  }

}
