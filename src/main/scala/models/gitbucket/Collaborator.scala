package models.gitbucket

import scalikejdbc._

case class Collaborator(
  userName: String,
  repositoryName: String,
  collaboratorName: String) {

  def save()(implicit session: DBSession = Collaborator.autoSession): Collaborator = Collaborator.save(this)(session)

  def destroy()(implicit session: DBSession = Collaborator.autoSession): Unit = Collaborator.destroy(this)(session)

}


object Collaborator extends SQLSyntaxSupport[Collaborator] {

  override val tableName = "COLLABORATOR"

  override val columns = Seq("USER_NAME", "REPOSITORY_NAME", "COLLABORATOR_NAME")

  def apply(c: SyntaxProvider[Collaborator])(rs: WrappedResultSet): Collaborator = apply(c.resultName)(rs)
  def apply(c: ResultName[Collaborator])(rs: WrappedResultSet): Collaborator = new Collaborator(
    userName = rs.get(c.userName),
    repositoryName = rs.get(c.repositoryName),
    collaboratorName = rs.get(c.collaboratorName)
  )

  val c = Collaborator.syntax("c")

  override val autoSession = AutoSession

  def find(collaboratorName: String, repositoryName: String, userName: String)(implicit session: DBSession = autoSession): Option[Collaborator] = {
    withSQL {
      select.from(Collaborator as c).where.eq(c.collaboratorName, collaboratorName).and.eq(c.repositoryName, repositoryName).and.eq(c.userName, userName)
    }.map(Collaborator(c.resultName)).single.apply()
  }

  def findAll()(implicit session: DBSession = autoSession): List[Collaborator] = {
    withSQL(select.from(Collaborator as c)).map(Collaborator(c.resultName)).list.apply()
  }

  def countAll()(implicit session: DBSession = autoSession): Long = {
    withSQL(select(sqls.count).from(Collaborator as c)).map(rs => rs.long(1)).single.apply().get
  }

  def findBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Option[Collaborator] = {
    withSQL {
      select.from(Collaborator as c).where.append(where)
    }.map(Collaborator(c.resultName)).single.apply()
  }

  def findAllBy(where: SQLSyntax)(implicit session: DBSession = autoSession): List[Collaborator] = {
    withSQL {
      select.from(Collaborator as c).where.append(where)
    }.map(Collaborator(c.resultName)).list.apply()
  }

  def countBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Long = {
    withSQL {
      select(sqls.count).from(Collaborator as c).where.append(where)
    }.map(_.long(1)).single.apply().get
  }

  def create(
    userName: String,
    repositoryName: String,
    collaboratorName: String)(implicit session: DBSession = autoSession): Collaborator = {
    withSQL {
      insert.into(Collaborator).columns(
        column.userName,
        column.repositoryName,
        column.collaboratorName
      ).values(
        userName,
        repositoryName,
        collaboratorName
      )
    }.update.apply()

    Collaborator(
      userName = userName,
      repositoryName = repositoryName,
      collaboratorName = collaboratorName)
  }

  def save(entity: Collaborator)(implicit session: DBSession = autoSession): Collaborator = {
    withSQL {
      update(Collaborator).set(
        column.userName -> entity.userName,
        column.repositoryName -> entity.repositoryName,
        column.collaboratorName -> entity.collaboratorName
      ).where.eq(column.collaboratorName, entity.collaboratorName).and.eq(column.repositoryName, entity.repositoryName).and.eq(column.userName, entity.userName)
    }.update.apply()
    entity
  }

  def destroy(entity: Collaborator)(implicit session: DBSession = autoSession): Unit = {
    withSQL { delete.from(Collaborator).where.eq(column.collaboratorName, entity.collaboratorName).and.eq(column.repositoryName, entity.repositoryName).and.eq(column.userName, entity.userName) }.update.apply()
  }

}
