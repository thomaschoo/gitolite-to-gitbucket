package models.gitbucket

import scalikejdbc._

case class GroupMember(
  groupName: String,
  userName: String,
  manager: Option[Boolean] = None) {

  def save()(implicit session: DBSession = GroupMember.autoSession): GroupMember = GroupMember.save(this)(session)

  def destroy()(implicit session: DBSession = GroupMember.autoSession): Unit = GroupMember.destroy(this)(session)

}


object GroupMember extends SQLSyntaxSupport[GroupMember] {

  override val tableName = "GROUP_MEMBER"

  override val columns = Seq("GROUP_NAME", "USER_NAME", "MANAGER")

  def apply(gm: SyntaxProvider[GroupMember])(rs: WrappedResultSet): GroupMember = apply(gm.resultName)(rs)
  def apply(gm: ResultName[GroupMember])(rs: WrappedResultSet): GroupMember = new GroupMember(
    groupName = rs.get(gm.groupName),
    userName = rs.get(gm.userName),
    manager = rs.get(gm.manager)
  )

  val gm = GroupMember.syntax("gm")

  override val autoSession = AutoSession

  def find(groupName: String, userName: String)(implicit session: DBSession = autoSession): Option[GroupMember] = {
    withSQL {
      select.from(GroupMember as gm).where.eq(gm.groupName, groupName).and.eq(gm.userName, userName)
    }.map(GroupMember(gm.resultName)).single.apply()
  }

  def findAll()(implicit session: DBSession = autoSession): List[GroupMember] = {
    withSQL(select.from(GroupMember as gm)).map(GroupMember(gm.resultName)).list.apply()
  }

  def countAll()(implicit session: DBSession = autoSession): Long = {
    withSQL(select(sqls.count).from(GroupMember as gm)).map(rs => rs.long(1)).single.apply().get
  }

  def findBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Option[GroupMember] = {
    withSQL {
      select.from(GroupMember as gm).where.append(where)
    }.map(GroupMember(gm.resultName)).single.apply()
  }

  def findAllBy(where: SQLSyntax)(implicit session: DBSession = autoSession): List[GroupMember] = {
    withSQL {
      select.from(GroupMember as gm).where.append(where)
    }.map(GroupMember(gm.resultName)).list.apply()
  }

  def countBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Long = {
    withSQL {
      select(sqls.count).from(GroupMember as gm).where.append(where)
    }.map(_.long(1)).single.apply().get
  }

  def create(
    groupName: String,
    userName: String,
    manager: Option[Boolean] = None)(implicit session: DBSession = autoSession): GroupMember = {
    withSQL {
      insert.into(GroupMember).columns(
        column.groupName,
        column.userName,
        column.manager
      ).values(
        groupName,
        userName,
        manager
      )
    }.update.apply()

    GroupMember(
      groupName = groupName,
      userName = userName,
      manager = manager)
  }

  def save(entity: GroupMember)(implicit session: DBSession = autoSession): GroupMember = {
    withSQL {
      update(GroupMember).set(
        column.groupName -> entity.groupName,
        column.userName -> entity.userName,
        column.manager -> entity.manager
      ).where.eq(column.groupName, entity.groupName).and.eq(column.userName, entity.userName)
    }.update.apply()
    entity
  }

  def destroy(entity: GroupMember)(implicit session: DBSession = autoSession): Unit = {
    withSQL { delete.from(GroupMember).where.eq(column.groupName, entity.groupName).and.eq(column.userName, entity.userName) }.update.apply()
  }

}
