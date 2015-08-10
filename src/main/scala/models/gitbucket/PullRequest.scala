package models.gitbucket

import scalikejdbc._

case class PullRequest(
  userName: String,
  repositoryName: String,
  issueId: Int,
  branch: String,
  requestUserName: String,
  requestRepositoryName: String,
  requestBranch: String,
  commitIdFrom: String,
  commitIdTo: String) {

  def save()(implicit session: DBSession = PullRequest.autoSession): PullRequest = PullRequest.save(this)(session)

  def destroy()(implicit session: DBSession = PullRequest.autoSession): Unit = PullRequest.destroy(this)(session)

}


object PullRequest extends SQLSyntaxSupport[PullRequest] {

  override val tableName = "PULL_REQUEST"

  override val columns = Seq("USER_NAME", "REPOSITORY_NAME", "ISSUE_ID", "BRANCH", "REQUEST_USER_NAME", "REQUEST_REPOSITORY_NAME", "REQUEST_BRANCH", "COMMIT_ID_FROM", "COMMIT_ID_TO")

  def apply(pr: SyntaxProvider[PullRequest])(rs: WrappedResultSet): PullRequest = apply(pr.resultName)(rs)
  def apply(pr: ResultName[PullRequest])(rs: WrappedResultSet): PullRequest = new PullRequest(
    userName = rs.get(pr.userName),
    repositoryName = rs.get(pr.repositoryName),
    issueId = rs.get(pr.issueId),
    branch = rs.get(pr.branch),
    requestUserName = rs.get(pr.requestUserName),
    requestRepositoryName = rs.get(pr.requestRepositoryName),
    requestBranch = rs.get(pr.requestBranch),
    commitIdFrom = rs.get(pr.commitIdFrom),
    commitIdTo = rs.get(pr.commitIdTo)
  )

  val pr = PullRequest.syntax("pr")

  override val autoSession = AutoSession

  def find(issueId: Int, repositoryName: String, userName: String)(implicit session: DBSession = autoSession): Option[PullRequest] = {
    withSQL {
      select.from(PullRequest as pr).where.eq(pr.issueId, issueId).and.eq(pr.repositoryName, repositoryName).and.eq(pr.userName, userName)
    }.map(PullRequest(pr.resultName)).single.apply()
  }

  def findAll()(implicit session: DBSession = autoSession): List[PullRequest] = {
    withSQL(select.from(PullRequest as pr)).map(PullRequest(pr.resultName)).list.apply()
  }

  def countAll()(implicit session: DBSession = autoSession): Long = {
    withSQL(select(sqls.count).from(PullRequest as pr)).map(rs => rs.long(1)).single.apply().get
  }

  def findBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Option[PullRequest] = {
    withSQL {
      select.from(PullRequest as pr).where.append(where)
    }.map(PullRequest(pr.resultName)).single.apply()
  }

  def findAllBy(where: SQLSyntax)(implicit session: DBSession = autoSession): List[PullRequest] = {
    withSQL {
      select.from(PullRequest as pr).where.append(where)
    }.map(PullRequest(pr.resultName)).list.apply()
  }

  def countBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Long = {
    withSQL {
      select(sqls.count).from(PullRequest as pr).where.append(where)
    }.map(_.long(1)).single.apply().get
  }

  def create(
    userName: String,
    repositoryName: String,
    issueId: Int,
    branch: String,
    requestUserName: String,
    requestRepositoryName: String,
    requestBranch: String,
    commitIdFrom: String,
    commitIdTo: String)(implicit session: DBSession = autoSession): PullRequest = {
    withSQL {
      insert.into(PullRequest).columns(
        column.userName,
        column.repositoryName,
        column.issueId,
        column.branch,
        column.requestUserName,
        column.requestRepositoryName,
        column.requestBranch,
        column.commitIdFrom,
        column.commitIdTo
      ).values(
        userName,
        repositoryName,
        issueId,
        branch,
        requestUserName,
        requestRepositoryName,
        requestBranch,
        commitIdFrom,
        commitIdTo
      )
    }.update.apply()

    PullRequest(
      userName = userName,
      repositoryName = repositoryName,
      issueId = issueId,
      branch = branch,
      requestUserName = requestUserName,
      requestRepositoryName = requestRepositoryName,
      requestBranch = requestBranch,
      commitIdFrom = commitIdFrom,
      commitIdTo = commitIdTo)
  }

  def save(entity: PullRequest)(implicit session: DBSession = autoSession): PullRequest = {
    withSQL {
      update(PullRequest).set(
        column.userName -> entity.userName,
        column.repositoryName -> entity.repositoryName,
        column.issueId -> entity.issueId,
        column.branch -> entity.branch,
        column.requestUserName -> entity.requestUserName,
        column.requestRepositoryName -> entity.requestRepositoryName,
        column.requestBranch -> entity.requestBranch,
        column.commitIdFrom -> entity.commitIdFrom,
        column.commitIdTo -> entity.commitIdTo
      ).where.eq(column.issueId, entity.issueId).and.eq(column.repositoryName, entity.repositoryName).and.eq(column.userName, entity.userName)
    }.update.apply()
    entity
  }

  def destroy(entity: PullRequest)(implicit session: DBSession = autoSession): Unit = {
    withSQL { delete.from(PullRequest).where.eq(column.issueId, entity.issueId).and.eq(column.repositoryName, entity.repositoryName).and.eq(column.userName, entity.userName) }.update.apply()
  }

}
