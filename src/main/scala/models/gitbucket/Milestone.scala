package models.gitbucket

import java.sql.Clob

import org.joda.time.DateTime

import scalikejdbc._

case class Milestone(
  userName: String,
  repositoryName: String,
  milestoneId: Int,
  title: String,
  description: Option[Clob] = None,
  dueDate: Option[DateTime] = None,
  closedDate: Option[DateTime] = None) {

  def save()(implicit session: DBSession = Milestone.autoSession): Milestone = Milestone.save(this)(session)

  def destroy()(implicit session: DBSession = Milestone.autoSession): Unit = Milestone.destroy(this)(session)

}


object Milestone extends SQLSyntaxSupport[Milestone] {

  override val tableName = "MILESTONE"

  override val columns = Seq("USER_NAME", "REPOSITORY_NAME", "MILESTONE_ID", "TITLE", "DESCRIPTION", "DUE_DATE", "CLOSED_DATE")

  def apply(m: SyntaxProvider[Milestone])(rs: WrappedResultSet): Milestone = apply(m.resultName)(rs)
  def apply(m: ResultName[Milestone])(rs: WrappedResultSet): Milestone = new Milestone(
    userName = rs.get(m.userName),
    repositoryName = rs.get(m.repositoryName),
    milestoneId = rs.get(m.milestoneId),
    title = rs.get(m.title),
    description = rs.get(m.description),
    dueDate = rs.get(m.dueDate),
    closedDate = rs.get(m.closedDate)
  )

  val m = Milestone.syntax("m")

  override val autoSession = AutoSession

  def find(milestoneId: Int, repositoryName: String, userName: String)(implicit session: DBSession = autoSession): Option[Milestone] = {
    withSQL {
      select.from(Milestone as m).where.eq(m.milestoneId, milestoneId).and.eq(m.repositoryName, repositoryName).and.eq(m.userName, userName)
    }.map(Milestone(m.resultName)).single.apply()
  }

  def findAll()(implicit session: DBSession = autoSession): List[Milestone] = {
    withSQL(select.from(Milestone as m)).map(Milestone(m.resultName)).list.apply()
  }

  def countAll()(implicit session: DBSession = autoSession): Long = {
    withSQL(select(sqls.count).from(Milestone as m)).map(rs => rs.long(1)).single.apply().get
  }

  def findBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Option[Milestone] = {
    withSQL {
      select.from(Milestone as m).where.append(where)
    }.map(Milestone(m.resultName)).single.apply()
  }

  def findAllBy(where: SQLSyntax)(implicit session: DBSession = autoSession): List[Milestone] = {
    withSQL {
      select.from(Milestone as m).where.append(where)
    }.map(Milestone(m.resultName)).list.apply()
  }

  def countBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Long = {
    withSQL {
      select(sqls.count).from(Milestone as m).where.append(where)
    }.map(_.long(1)).single.apply().get
  }

  def create(
    userName: String,
    repositoryName: String,
    title: String,
    description: Option[Clob] = None,
    dueDate: Option[DateTime] = None,
    closedDate: Option[DateTime] = None)(implicit session: DBSession = autoSession): Milestone = {
    val generatedKey = withSQL {
      insert.into(Milestone).columns(
        column.userName,
        column.repositoryName,
        column.title,
        column.description,
        column.dueDate,
        column.closedDate
      ).values(
        userName,
        repositoryName,
        title,
        description,
        dueDate,
        closedDate
      )
    }.updateAndReturnGeneratedKey.apply()

    Milestone(
      milestoneId = generatedKey.toInt,
      userName = userName,
      repositoryName = repositoryName,
      title = title,
      description = description,
      dueDate = dueDate,
      closedDate = closedDate)
  }

  def save(entity: Milestone)(implicit session: DBSession = autoSession): Milestone = {
    withSQL {
      update(Milestone).set(
        column.userName -> entity.userName,
        column.repositoryName -> entity.repositoryName,
        column.milestoneId -> entity.milestoneId,
        column.title -> entity.title,
        column.description -> entity.description,
        column.dueDate -> entity.dueDate,
        column.closedDate -> entity.closedDate
      ).where.eq(column.milestoneId, entity.milestoneId).and.eq(column.repositoryName, entity.repositoryName).and.eq(column.userName, entity.userName)
    }.update.apply()
    entity
  }

  def destroy(entity: Milestone)(implicit session: DBSession = autoSession): Unit = {
    withSQL { delete.from(Milestone).where.eq(column.milestoneId, entity.milestoneId).and.eq(column.repositoryName, entity.repositoryName).and.eq(column.userName, entity.userName) }.update.apply()
  }

}
