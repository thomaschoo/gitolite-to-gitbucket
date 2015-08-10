package models.gitbucket

import java.sql.Clob

import scalikejdbc._

case class SshKey(
  userName: String,
  sshKeyId: Int,
  title: String,
  publicKey: Clob) {

  def save()(implicit session: DBSession = SshKey.autoSession): SshKey = SshKey.save(this)(session)

  def destroy()(implicit session: DBSession = SshKey.autoSession): Unit = SshKey.destroy(this)(session)

}


object SshKey extends SQLSyntaxSupport[SshKey] {

  override val tableName = "SSH_KEY"

  override val columns = Seq("USER_NAME", "SSH_KEY_ID", "TITLE", "PUBLIC_KEY")

  def apply(sk: SyntaxProvider[SshKey])(rs: WrappedResultSet): SshKey = apply(sk.resultName)(rs)
  def apply(sk: ResultName[SshKey])(rs: WrappedResultSet): SshKey = new SshKey(
    userName = rs.get(sk.userName),
    sshKeyId = rs.get(sk.sshKeyId),
    title = rs.get(sk.title),
    publicKey = rs.get(sk.publicKey)
  )

  val sk = SshKey.syntax("sk")

  override val autoSession = AutoSession

  def find(sshKeyId: Int, userName: String)(implicit session: DBSession = autoSession): Option[SshKey] = {
    withSQL {
      select.from(SshKey as sk).where.eq(sk.sshKeyId, sshKeyId).and.eq(sk.userName, userName)
    }.map(SshKey(sk.resultName)).single.apply()
  }

  def findAll()(implicit session: DBSession = autoSession): List[SshKey] = {
    withSQL(select.from(SshKey as sk)).map(SshKey(sk.resultName)).list.apply()
  }

  def countAll()(implicit session: DBSession = autoSession): Long = {
    withSQL(select(sqls.count).from(SshKey as sk)).map(rs => rs.long(1)).single.apply().get
  }

  def findBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Option[SshKey] = {
    withSQL {
      select.from(SshKey as sk).where.append(where)
    }.map(SshKey(sk.resultName)).single.apply()
  }

  def findAllBy(where: SQLSyntax)(implicit session: DBSession = autoSession): List[SshKey] = {
    withSQL {
      select.from(SshKey as sk).where.append(where)
    }.map(SshKey(sk.resultName)).list.apply()
  }

  def countBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Long = {
    withSQL {
      select(sqls.count).from(SshKey as sk).where.append(where)
    }.map(_.long(1)).single.apply().get
  }

  def create(
    userName: String,
    title: String,
    publicKey: Clob)(implicit session: DBSession = autoSession): SshKey = {
    val generatedKey = withSQL {
      insert.into(SshKey).columns(
        column.userName,
        column.title,
        column.publicKey
      ).values(
        userName,
        title,
        publicKey
      )
    }.updateAndReturnGeneratedKey.apply()

    SshKey(
      sshKeyId = generatedKey.toInt,
      userName = userName,
      title = title,
      publicKey = publicKey)
  }

  def save(entity: SshKey)(implicit session: DBSession = autoSession): SshKey = {
    withSQL {
      update(SshKey).set(
        column.userName -> entity.userName,
        column.sshKeyId -> entity.sshKeyId,
        column.title -> entity.title,
        column.publicKey -> entity.publicKey
      ).where.eq(column.sshKeyId, entity.sshKeyId).and.eq(column.userName, entity.userName)
    }.update.apply()
    entity
  }

  def destroy(entity: SshKey)(implicit session: DBSession = autoSession): Unit = {
    withSQL { delete.from(SshKey).where.eq(column.sshKeyId, entity.sshKeyId).and.eq(column.userName, entity.userName) }.update.apply()
  }

}
