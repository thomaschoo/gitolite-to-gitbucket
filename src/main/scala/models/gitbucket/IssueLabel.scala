package models.gitbucket

import scalikejdbc._

case class IssueLabel(
  userName: String,
  repositoryName: String,
  issueId: Int,
  labelId: Int) {

  def save()(implicit session: DBSession = IssueLabel.autoSession): IssueLabel = IssueLabel.save(this)(session)

  def destroy()(implicit session: DBSession = IssueLabel.autoSession): Unit = IssueLabel.destroy(this)(session)

}


object IssueLabel extends SQLSyntaxSupport[IssueLabel] {

  override val tableName = "ISSUE_LABEL"

  override val columns = Seq("USER_NAME", "REPOSITORY_NAME", "ISSUE_ID", "LABEL_ID")

  def apply(il: SyntaxProvider[IssueLabel])(rs: WrappedResultSet): IssueLabel = apply(il.resultName)(rs)
  def apply(il: ResultName[IssueLabel])(rs: WrappedResultSet): IssueLabel = new IssueLabel(
    userName = rs.get(il.userName),
    repositoryName = rs.get(il.repositoryName),
    issueId = rs.get(il.issueId),
    labelId = rs.get(il.labelId)
  )

  val il = IssueLabel.syntax("il")

  override val autoSession = AutoSession

  def find(issueId: Int, labelId: Int, repositoryName: String, userName: String)(implicit session: DBSession = autoSession): Option[IssueLabel] = {
    withSQL {
      select.from(IssueLabel as il).where.eq(il.issueId, issueId).and.eq(il.labelId, labelId).and.eq(il.repositoryName, repositoryName).and.eq(il.userName, userName)
    }.map(IssueLabel(il.resultName)).single.apply()
  }

  def findAll()(implicit session: DBSession = autoSession): List[IssueLabel] = {
    withSQL(select.from(IssueLabel as il)).map(IssueLabel(il.resultName)).list.apply()
  }

  def countAll()(implicit session: DBSession = autoSession): Long = {
    withSQL(select(sqls.count).from(IssueLabel as il)).map(rs => rs.long(1)).single.apply().get
  }

  def findBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Option[IssueLabel] = {
    withSQL {
      select.from(IssueLabel as il).where.append(where)
    }.map(IssueLabel(il.resultName)).single.apply()
  }

  def findAllBy(where: SQLSyntax)(implicit session: DBSession = autoSession): List[IssueLabel] = {
    withSQL {
      select.from(IssueLabel as il).where.append(where)
    }.map(IssueLabel(il.resultName)).list.apply()
  }

  def countBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Long = {
    withSQL {
      select(sqls.count).from(IssueLabel as il).where.append(where)
    }.map(_.long(1)).single.apply().get
  }

  def create(
    userName: String,
    repositoryName: String,
    issueId: Int,
    labelId: Int)(implicit session: DBSession = autoSession): IssueLabel = {
    withSQL {
      insert.into(IssueLabel).columns(
        column.userName,
        column.repositoryName,
        column.issueId,
        column.labelId
      ).values(
        userName,
        repositoryName,
        issueId,
        labelId
      )
    }.update.apply()

    IssueLabel(
      userName = userName,
      repositoryName = repositoryName,
      issueId = issueId,
      labelId = labelId)
  }

  def save(entity: IssueLabel)(implicit session: DBSession = autoSession): IssueLabel = {
    withSQL {
      update(IssueLabel).set(
        column.userName -> entity.userName,
        column.repositoryName -> entity.repositoryName,
        column.issueId -> entity.issueId,
        column.labelId -> entity.labelId
      ).where.eq(column.issueId, entity.issueId).and.eq(column.labelId, entity.labelId).and.eq(column.repositoryName, entity.repositoryName).and.eq(column.userName, entity.userName)
    }.update.apply()
    entity
  }

  def destroy(entity: IssueLabel)(implicit session: DBSession = autoSession): Unit = {
    withSQL { delete.from(IssueLabel).where.eq(column.issueId, entity.issueId).and.eq(column.labelId, entity.labelId).and.eq(column.repositoryName, entity.repositoryName).and.eq(column.userName, entity.userName) }.update.apply()
  }

}
