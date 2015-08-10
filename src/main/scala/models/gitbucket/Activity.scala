package models.gitbucket

import java.sql.Clob

import org.joda.time.DateTime

import scalikejdbc._

case class Activity(
  activityId: Int,
  userName: String,
  repositoryName: String,
  activityUserName: String,
  activityType: String,
  message: Clob,
  additionalInfo: Option[Clob] = None,
  activityDate: DateTime) {

  def save()(implicit session: DBSession = Activity.autoSession): Activity = Activity.save(this)(session)

  def destroy()(implicit session: DBSession = Activity.autoSession): Unit = Activity.destroy(this)(session)

}


object Activity extends SQLSyntaxSupport[Activity] {

  override val tableName = "ACTIVITY"

  override val columns = Seq("ACTIVITY_ID", "USER_NAME", "REPOSITORY_NAME", "ACTIVITY_USER_NAME", "ACTIVITY_TYPE", "MESSAGE", "ADDITIONAL_INFO", "ACTIVITY_DATE")

  def apply(a: SyntaxProvider[Activity])(rs: WrappedResultSet): Activity = apply(a.resultName)(rs)
  def apply(a: ResultName[Activity])(rs: WrappedResultSet): Activity = new Activity(
    activityId = rs.get(a.activityId),
    userName = rs.get(a.userName),
    repositoryName = rs.get(a.repositoryName),
    activityUserName = rs.get(a.activityUserName),
    activityType = rs.get(a.activityType),
    message = rs.get(a.message),
    additionalInfo = rs.get(a.additionalInfo),
    activityDate = rs.get(a.activityDate)
  )

  val a = Activity.syntax("a")

  override val autoSession = AutoSession

  def find(activityId: Int)(implicit session: DBSession = autoSession): Option[Activity] = {
    withSQL {
      select.from(Activity as a).where.eq(a.activityId, activityId)
    }.map(Activity(a.resultName)).single.apply()
  }

  def findAll()(implicit session: DBSession = autoSession): List[Activity] = {
    withSQL(select.from(Activity as a)).map(Activity(a.resultName)).list.apply()
  }

  def countAll()(implicit session: DBSession = autoSession): Long = {
    withSQL(select(sqls.count).from(Activity as a)).map(rs => rs.long(1)).single.apply().get
  }

  def findBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Option[Activity] = {
    withSQL {
      select.from(Activity as a).where.append(where)
    }.map(Activity(a.resultName)).single.apply()
  }

  def findAllBy(where: SQLSyntax)(implicit session: DBSession = autoSession): List[Activity] = {
    withSQL {
      select.from(Activity as a).where.append(where)
    }.map(Activity(a.resultName)).list.apply()
  }

  def countBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Long = {
    withSQL {
      select(sqls.count).from(Activity as a).where.append(where)
    }.map(_.long(1)).single.apply().get
  }

  def create(
    userName: String,
    repositoryName: String,
    activityUserName: String,
    activityType: String,
    message: Clob,
    additionalInfo: Option[Clob] = None,
    activityDate: DateTime)(implicit session: DBSession = autoSession): Activity = {
    val generatedKey = withSQL {
      insert.into(Activity).columns(
        column.userName,
        column.repositoryName,
        column.activityUserName,
        column.activityType,
        column.message,
        column.additionalInfo,
        column.activityDate
      ).values(
        userName,
        repositoryName,
        activityUserName,
        activityType,
        message,
        additionalInfo,
        activityDate
      )
    }.updateAndReturnGeneratedKey.apply()

    Activity(
      activityId = generatedKey.toInt,
      userName = userName,
      repositoryName = repositoryName,
      activityUserName = activityUserName,
      activityType = activityType,
      message = message,
      additionalInfo = additionalInfo,
      activityDate = activityDate)
  }

  def save(entity: Activity)(implicit session: DBSession = autoSession): Activity = {
    withSQL {
      update(Activity).set(
        column.activityId -> entity.activityId,
        column.userName -> entity.userName,
        column.repositoryName -> entity.repositoryName,
        column.activityUserName -> entity.activityUserName,
        column.activityType -> entity.activityType,
        column.message -> entity.message,
        column.additionalInfo -> entity.additionalInfo,
        column.activityDate -> entity.activityDate
      ).where.eq(column.activityId, entity.activityId)
    }.update.apply()
    entity
  }

  def destroy(entity: Activity)(implicit session: DBSession = autoSession): Unit = {
    withSQL { delete.from(Activity).where.eq(column.activityId, entity.activityId) }.update.apply()
  }

}
