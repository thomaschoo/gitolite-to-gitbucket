package models.gitbucket

import java.sql.Clob

import scalikejdbc._

case class AccessToken(
  accessTokenId: Int,
  tokenHash: String,
  userName: String,
  note: Clob) {

  def save()(implicit session: DBSession = AccessToken.autoSession): AccessToken = AccessToken.save(this)(session)

  def destroy()(implicit session: DBSession = AccessToken.autoSession): Unit = AccessToken.destroy(this)(session)

}


object AccessToken extends SQLSyntaxSupport[AccessToken] {

  override val tableName = "ACCESS_TOKEN"

  override val columns = Seq("ACCESS_TOKEN_ID", "TOKEN_HASH", "USER_NAME", "NOTE")

  def apply(at: SyntaxProvider[AccessToken])(rs: WrappedResultSet): AccessToken = apply(at.resultName)(rs)
  def apply(at: ResultName[AccessToken])(rs: WrappedResultSet): AccessToken = new AccessToken(
    accessTokenId = rs.get(at.accessTokenId),
    tokenHash = rs.get(at.tokenHash),
    userName = rs.get(at.userName),
    note = rs.get(at.note)
  )

  val at = AccessToken.syntax("at")

  override val autoSession = AutoSession

  def find(accessTokenId: Int)(implicit session: DBSession = autoSession): Option[AccessToken] = {
    withSQL {
      select.from(AccessToken as at).where.eq(at.accessTokenId, accessTokenId)
    }.map(AccessToken(at.resultName)).single.apply()
  }

  def findAll()(implicit session: DBSession = autoSession): List[AccessToken] = {
    withSQL(select.from(AccessToken as at)).map(AccessToken(at.resultName)).list.apply()
  }

  def countAll()(implicit session: DBSession = autoSession): Long = {
    withSQL(select(sqls.count).from(AccessToken as at)).map(rs => rs.long(1)).single.apply().get
  }

  def findBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Option[AccessToken] = {
    withSQL {
      select.from(AccessToken as at).where.append(where)
    }.map(AccessToken(at.resultName)).single.apply()
  }

  def findAllBy(where: SQLSyntax)(implicit session: DBSession = autoSession): List[AccessToken] = {
    withSQL {
      select.from(AccessToken as at).where.append(where)
    }.map(AccessToken(at.resultName)).list.apply()
  }

  def countBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Long = {
    withSQL {
      select(sqls.count).from(AccessToken as at).where.append(where)
    }.map(_.long(1)).single.apply().get
  }

  def create(
    tokenHash: String,
    userName: String,
    note: Clob)(implicit session: DBSession = autoSession): AccessToken = {
    val generatedKey = withSQL {
      insert.into(AccessToken).columns(
        column.tokenHash,
        column.userName,
        column.note
      ).values(
        tokenHash,
        userName,
        note
      )
    }.updateAndReturnGeneratedKey.apply()

    AccessToken(
      accessTokenId = generatedKey.toInt,
      tokenHash = tokenHash,
      userName = userName,
      note = note)
  }

  def save(entity: AccessToken)(implicit session: DBSession = autoSession): AccessToken = {
    withSQL {
      update(AccessToken).set(
        column.accessTokenId -> entity.accessTokenId,
        column.tokenHash -> entity.tokenHash,
        column.userName -> entity.userName,
        column.note -> entity.note
      ).where.eq(column.accessTokenId, entity.accessTokenId)
    }.update.apply()
    entity
  }

  def destroy(entity: AccessToken)(implicit session: DBSession = autoSession): Unit = {
    withSQL { delete.from(AccessToken).where.eq(column.accessTokenId, entity.accessTokenId) }.update.apply()
  }

}
