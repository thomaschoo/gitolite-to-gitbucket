package models.gitbucket

import org.joda.time.DateTime

import scalikejdbc._

case class Account(
  userName: String,
  mailAddress: String,
  password: String,
  administrator: Boolean,
  url: Option[String] = None,
  registeredDate: DateTime,
  updatedDate: DateTime,
  lastLoginDate: Option[DateTime] = None,
  image: Option[String] = None,
  groupAccount: Boolean,
  fullName: String,
  removed: Option[Boolean] = None) {

  def save()(implicit session: DBSession = Account.autoSession): Account = Account.save(this)(session)

  def destroy()(implicit session: DBSession = Account.autoSession): Unit = Account.destroy(this)(session)

}


object Account extends SQLSyntaxSupport[Account] {

  override val tableName = "ACCOUNT"

  override val columns = Seq("USER_NAME", "MAIL_ADDRESS", "PASSWORD", "ADMINISTRATOR", "URL", "REGISTERED_DATE", "UPDATED_DATE", "LAST_LOGIN_DATE", "IMAGE", "GROUP_ACCOUNT", "FULL_NAME", "REMOVED")

  def apply(a: SyntaxProvider[Account])(rs: WrappedResultSet): Account = apply(a.resultName)(rs)
  def apply(a: ResultName[Account])(rs: WrappedResultSet): Account = new Account(
    userName = rs.get(a.userName),
    mailAddress = rs.get(a.mailAddress),
    password = rs.get(a.password),
    administrator = rs.get(a.administrator),
    url = rs.get(a.url),
    registeredDate = rs.get(a.registeredDate),
    updatedDate = rs.get(a.updatedDate),
    lastLoginDate = rs.get(a.lastLoginDate),
    image = rs.get(a.image),
    groupAccount = rs.get(a.groupAccount),
    fullName = rs.get(a.fullName),
    removed = rs.get(a.removed)
  )

  val a = Account.syntax("a")

  override val autoSession = AutoSession

  def find(userName: String)(implicit session: DBSession = autoSession): Option[Account] = {
    withSQL {
      select.from(Account as a).where.eq(a.userName, userName)
    }.map(Account(a.resultName)).single.apply()
  }

  def findAll()(implicit session: DBSession = autoSession): List[Account] = {
    withSQL(select.from(Account as a)).map(Account(a.resultName)).list.apply()
  }

  def countAll()(implicit session: DBSession = autoSession): Long = {
    withSQL(select(sqls.count).from(Account as a)).map(rs => rs.long(1)).single.apply().get
  }

  def findBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Option[Account] = {
    withSQL {
      select.from(Account as a).where.append(where)
    }.map(Account(a.resultName)).single.apply()
  }

  def findAllBy(where: SQLSyntax)(implicit session: DBSession = autoSession): List[Account] = {
    withSQL {
      select.from(Account as a).where.append(where)
    }.map(Account(a.resultName)).list.apply()
  }

  def countBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Long = {
    withSQL {
      select(sqls.count).from(Account as a).where.append(where)
    }.map(_.long(1)).single.apply().get
  }

  def create(
    userName: String,
    mailAddress: String,
    password: String,
    administrator: Boolean,
    url: Option[String] = None,
    registeredDate: DateTime,
    updatedDate: DateTime,
    lastLoginDate: Option[DateTime] = None,
    image: Option[String] = None,
    groupAccount: Boolean,
    fullName: String,
    removed: Option[Boolean] = None)(implicit session: DBSession = autoSession): Account = {
    withSQL {
      insert.into(Account).columns(
        column.userName,
        column.mailAddress,
        column.password,
        column.administrator,
        column.url,
        column.registeredDate,
        column.updatedDate,
        column.lastLoginDate,
        column.image,
        column.groupAccount,
        column.fullName,
        column.removed
      ).values(
        userName,
        mailAddress,
        password,
        administrator,
        url,
        registeredDate,
        updatedDate,
        lastLoginDate,
        image,
        groupAccount,
        fullName,
        removed
      )
    }.update.apply()

    Account(
      userName = userName,
      mailAddress = mailAddress,
      password = password,
      administrator = administrator,
      url = url,
      registeredDate = registeredDate,
      updatedDate = updatedDate,
      lastLoginDate = lastLoginDate,
      image = image,
      groupAccount = groupAccount,
      fullName = fullName,
      removed = removed)
  }

  def save(entity: Account)(implicit session: DBSession = autoSession): Account = {
    withSQL {
      update(Account).set(
        column.userName -> entity.userName,
        column.mailAddress -> entity.mailAddress,
        column.password -> entity.password,
        column.administrator -> entity.administrator,
        column.url -> entity.url,
        column.registeredDate -> entity.registeredDate,
        column.updatedDate -> entity.updatedDate,
        column.lastLoginDate -> entity.lastLoginDate,
        column.image -> entity.image,
        column.groupAccount -> entity.groupAccount,
        column.fullName -> entity.fullName,
        column.removed -> entity.removed
      ).where.eq(column.userName, entity.userName)
    }.update.apply()
    entity
  }

  def destroy(entity: Account)(implicit session: DBSession = autoSession): Unit = {
    withSQL { delete.from(Account).where.eq(column.userName, entity.userName) }.update.apply()
  }

}
