package models.gitbucket

import scalikejdbc._

case class IssueId(
  userName: String,
  repositoryName: String,
  issueId: Int) {

  def save()(implicit session: DBSession = IssueId.autoSession): IssueId = IssueId.save(this)(session)

  def destroy()(implicit session: DBSession = IssueId.autoSession): Unit = IssueId.destroy(this)(session)

}


object IssueId extends SQLSyntaxSupport[IssueId] {

  override val tableName = "ISSUE_ID"

  override val columns = Seq("USER_NAME", "REPOSITORY_NAME", "ISSUE_ID")

  def apply(ii: SyntaxProvider[IssueId])(rs: WrappedResultSet): IssueId = apply(ii.resultName)(rs)
  def apply(ii: ResultName[IssueId])(rs: WrappedResultSet): IssueId = new IssueId(
    userName = rs.get(ii.userName),
    repositoryName = rs.get(ii.repositoryName),
    issueId = rs.get(ii.issueId)
  )

  val ii = IssueId.syntax("ii")

  override val autoSession = AutoSession

  def find(repositoryName: String, userName: String)(implicit session: DBSession = autoSession): Option[IssueId] = {
    withSQL {
      select.from(IssueId as ii).where.eq(ii.repositoryName, repositoryName).and.eq(ii.userName, userName)
    }.map(IssueId(ii.resultName)).single.apply()
  }

  def findAll()(implicit session: DBSession = autoSession): List[IssueId] = {
    withSQL(select.from(IssueId as ii)).map(IssueId(ii.resultName)).list.apply()
  }

  def countAll()(implicit session: DBSession = autoSession): Long = {
    withSQL(select(sqls.count).from(IssueId as ii)).map(rs => rs.long(1)).single.apply().get
  }

  def findBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Option[IssueId] = {
    withSQL {
      select.from(IssueId as ii).where.append(where)
    }.map(IssueId(ii.resultName)).single.apply()
  }

  def findAllBy(where: SQLSyntax)(implicit session: DBSession = autoSession): List[IssueId] = {
    withSQL {
      select.from(IssueId as ii).where.append(where)
    }.map(IssueId(ii.resultName)).list.apply()
  }

  def countBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Long = {
    withSQL {
      select(sqls.count).from(IssueId as ii).where.append(where)
    }.map(_.long(1)).single.apply().get
  }

  def create(
    userName: String,
    repositoryName: String,
    issueId: Int)(implicit session: DBSession = autoSession): IssueId = {
    withSQL {
      insert.into(IssueId).columns(
        column.userName,
        column.repositoryName,
        column.issueId
      ).values(
        userName,
        repositoryName,
        issueId
      )
    }.update.apply()

    IssueId(
      userName = userName,
      repositoryName = repositoryName,
      issueId = issueId)
  }

  def save(entity: IssueId)(implicit session: DBSession = autoSession): IssueId = {
    withSQL {
      update(IssueId).set(
        column.userName -> entity.userName,
        column.repositoryName -> entity.repositoryName,
        column.issueId -> entity.issueId
      ).where.eq(column.repositoryName, entity.repositoryName).and.eq(column.userName, entity.userName)
    }.update.apply()
    entity
  }

  def destroy(entity: IssueId)(implicit session: DBSession = autoSession): Unit = {
    withSQL { delete.from(IssueId).where.eq(column.repositoryName, entity.repositoryName).and.eq(column.userName, entity.userName) }.update.apply()
  }

}
