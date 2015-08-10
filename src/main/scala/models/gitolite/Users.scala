package models.gitolite

import org.joda.time.DateTime

import scalikejdbc._

case class Users(
  id: Int,
  email: String,
  encryptedPassword: String,
  resetPasswordToken: Option[String] = None,
  resetPasswordSentAt: Option[DateTime] = None,
  rememberCreatedAt: Option[DateTime] = None,
  signInCount: Option[Int] = None,
  currentSignInAt: Option[DateTime] = None,
  lastSignInAt: Option[DateTime] = None,
  currentSignInIp: Option[String] = None,
  lastSignInIp: Option[String] = None,
  createdAt: DateTime,
  updatedAt: DateTime,
  name: Option[String] = None,
  admin: Boolean,
  projectsLimit: Option[Int] = None,
  skype: String,
  linkedin: String,
  twitter: String,
  authenticationToken: Option[String] = None,
  darkScheme: Boolean,
  themeId: Int,
  bio: Option[String] = None,
  blocked: Boolean,
  tid: Option[String] = None,
  firstName: Option[String] = None,
  lastName: Option[String] = None,
  language: Option[String] = None,
  lastLogin: Option[DateTime] = None) {

  def save()(implicit session: DBSession = Users.autoSession): Users = Users.save(this)(session)

  def destroy()(implicit session: DBSession = Users.autoSession): Unit = Users.destroy(this)(session)

}


object Users extends SQLSyntaxSupport[Users] {

  override val tableName = "users"

  override val columns = Seq("id", "email", "encrypted_password", "reset_password_token", "reset_password_sent_at", "remember_created_at", "sign_in_count", "current_sign_in_at", "last_sign_in_at", "current_sign_in_ip", "last_sign_in_ip", "created_at", "updated_at", "name", "admin", "projects_limit", "skype", "linkedin", "twitter", "authentication_token", "dark_scheme", "theme_id", "bio", "blocked", "tid", "first_name", "last_name", "language", "last_login")

  def apply(u: SyntaxProvider[Users])(rs: WrappedResultSet): Users = apply(u.resultName)(rs)
  def apply(u: ResultName[Users])(rs: WrappedResultSet): Users = new Users(
    id = rs.get(u.id),
    email = rs.get(u.email),
    encryptedPassword = rs.get(u.encryptedPassword),
    resetPasswordToken = rs.get(u.resetPasswordToken),
    resetPasswordSentAt = rs.get(u.resetPasswordSentAt),
    rememberCreatedAt = rs.get(u.rememberCreatedAt),
    signInCount = rs.get(u.signInCount),
    currentSignInAt = rs.get(u.currentSignInAt),
    lastSignInAt = rs.get(u.lastSignInAt),
    currentSignInIp = rs.get(u.currentSignInIp),
    lastSignInIp = rs.get(u.lastSignInIp),
    createdAt = rs.get(u.createdAt),
    updatedAt = rs.get(u.updatedAt),
    name = rs.get(u.name),
    admin = rs.get(u.admin),
    projectsLimit = rs.get(u.projectsLimit),
    skype = rs.get(u.skype),
    linkedin = rs.get(u.linkedin),
    twitter = rs.get(u.twitter),
    authenticationToken = rs.get(u.authenticationToken),
    darkScheme = rs.get(u.darkScheme),
    themeId = rs.get(u.themeId),
    bio = rs.get(u.bio),
    blocked = rs.get(u.blocked),
    tid = rs.get(u.tid),
    firstName = rs.get(u.firstName),
    lastName = rs.get(u.lastName),
    language = rs.get(u.language),
    lastLogin = rs.get(u.lastLogin)
  )

  val u = Users.syntax("u")

  override val autoSession = AutoSession

  def find(id: Int)(implicit session: DBSession = autoSession): Option[Users] = {
    withSQL {
      select.from(Users as u).where.eq(u.id, id)
    }.map(Users(u.resultName)).single.apply()
  }

  def findAll()(implicit session: DBSession = autoSession): List[Users] = {
    withSQL(select.from(Users as u)).map(Users(u.resultName)).list.apply()
  }

  def countAll()(implicit session: DBSession = autoSession): Long = {
    withSQL(select(sqls.count).from(Users as u)).map(rs => rs.long(1)).single.apply().get
  }

  def findBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Option[Users] = {
    withSQL {
      select.from(Users as u).where.append(where)
    }.map(Users(u.resultName)).single.apply()
  }

  def findAllBy(where: SQLSyntax)(implicit session: DBSession = autoSession): List[Users] = {
    withSQL {
      select.from(Users as u).where.append(where)
    }.map(Users(u.resultName)).list.apply()
  }

  def countBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Long = {
    withSQL {
      select(sqls.count).from(Users as u).where.append(where)
    }.map(_.long(1)).single.apply().get
  }

  def create(
    email: String,
    encryptedPassword: String,
    resetPasswordToken: Option[String] = None,
    resetPasswordSentAt: Option[DateTime] = None,
    rememberCreatedAt: Option[DateTime] = None,
    signInCount: Option[Int] = None,
    currentSignInAt: Option[DateTime] = None,
    lastSignInAt: Option[DateTime] = None,
    currentSignInIp: Option[String] = None,
    lastSignInIp: Option[String] = None,
    createdAt: DateTime,
    updatedAt: DateTime,
    name: Option[String] = None,
    admin: Boolean,
    projectsLimit: Option[Int] = None,
    skype: String,
    linkedin: String,
    twitter: String,
    authenticationToken: Option[String] = None,
    darkScheme: Boolean,
    themeId: Int,
    bio: Option[String] = None,
    blocked: Boolean,
    tid: Option[String] = None,
    firstName: Option[String] = None,
    lastName: Option[String] = None,
    language: Option[String] = None,
    lastLogin: Option[DateTime] = None)(implicit session: DBSession = autoSession): Users = {
    val generatedKey = withSQL {
      insert.into(Users).columns(
        column.email,
        column.encryptedPassword,
        column.resetPasswordToken,
        column.resetPasswordSentAt,
        column.rememberCreatedAt,
        column.signInCount,
        column.currentSignInAt,
        column.lastSignInAt,
        column.currentSignInIp,
        column.lastSignInIp,
        column.createdAt,
        column.updatedAt,
        column.name,
        column.admin,
        column.projectsLimit,
        column.skype,
        column.linkedin,
        column.twitter,
        column.authenticationToken,
        column.darkScheme,
        column.themeId,
        column.bio,
        column.blocked,
        column.tid,
        column.firstName,
        column.lastName,
        column.language,
        column.lastLogin
      ).values(
        email,
        encryptedPassword,
        resetPasswordToken,
        resetPasswordSentAt,
        rememberCreatedAt,
        signInCount,
        currentSignInAt,
        lastSignInAt,
        currentSignInIp,
        lastSignInIp,
        createdAt,
        updatedAt,
        name,
        admin,
        projectsLimit,
        skype,
        linkedin,
        twitter,
        authenticationToken,
        darkScheme,
        themeId,
        bio,
        blocked,
        tid,
        firstName,
        lastName,
        language,
        lastLogin
      )
    }.updateAndReturnGeneratedKey.apply()

    new Users(
      id = generatedKey.toInt,
      email = email,
      encryptedPassword = encryptedPassword,
      resetPasswordToken = resetPasswordToken,
      resetPasswordSentAt = resetPasswordSentAt,
      rememberCreatedAt = rememberCreatedAt,
      signInCount = signInCount,
      currentSignInAt = currentSignInAt,
      lastSignInAt = lastSignInAt,
      currentSignInIp = currentSignInIp,
      lastSignInIp = lastSignInIp,
      createdAt = createdAt,
      updatedAt = updatedAt,
      name = name,
      admin = admin,
      projectsLimit = projectsLimit,
      skype = skype,
      linkedin = linkedin,
      twitter = twitter,
      authenticationToken = authenticationToken,
      darkScheme = darkScheme,
      themeId = themeId,
      bio = bio,
      blocked = blocked,
      tid = tid,
      firstName = firstName,
      lastName = lastName,
      language = language,
      lastLogin = lastLogin)
  }

  def save(entity: Users)(implicit session: DBSession = autoSession): Users = {
    withSQL {
      update(Users).set(
        column.id -> entity.id,
        column.email -> entity.email,
        column.encryptedPassword -> entity.encryptedPassword,
        column.resetPasswordToken -> entity.resetPasswordToken,
        column.resetPasswordSentAt -> entity.resetPasswordSentAt,
        column.rememberCreatedAt -> entity.rememberCreatedAt,
        column.signInCount -> entity.signInCount,
        column.currentSignInAt -> entity.currentSignInAt,
        column.lastSignInAt -> entity.lastSignInAt,
        column.currentSignInIp -> entity.currentSignInIp,
        column.lastSignInIp -> entity.lastSignInIp,
        column.createdAt -> entity.createdAt,
        column.updatedAt -> entity.updatedAt,
        column.name -> entity.name,
        column.admin -> entity.admin,
        column.projectsLimit -> entity.projectsLimit,
        column.skype -> entity.skype,
        column.linkedin -> entity.linkedin,
        column.twitter -> entity.twitter,
        column.authenticationToken -> entity.authenticationToken,
        column.darkScheme -> entity.darkScheme,
        column.themeId -> entity.themeId,
        column.bio -> entity.bio,
        column.blocked -> entity.blocked,
        column.tid -> entity.tid,
        column.firstName -> entity.firstName,
        column.lastName -> entity.lastName,
        column.language -> entity.language,
        column.lastLogin -> entity.lastLogin
      ).where.eq(column.id, entity.id)
    }.update.apply()
    entity
  }

  def destroy(entity: Users)(implicit session: DBSession = autoSession): Unit = {
    withSQL { delete.from(Users).where.eq(column.id, entity.id) }.update.apply()
  }

}
