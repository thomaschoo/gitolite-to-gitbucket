package models.gitbucket

import scalikejdbc._

case class Label(
  userName: String,
  repositoryName: String,
  labelId: Int,
  labelName: String,
  color: String) {

  def save()(implicit session: DBSession = Label.autoSession): Label = Label.save(this)(session)

  def destroy()(implicit session: DBSession = Label.autoSession): Unit = Label.destroy(this)(session)

}


object Label extends SQLSyntaxSupport[Label] {

  override val tableName = "LABEL"

  override val columns = Seq("USER_NAME", "REPOSITORY_NAME", "LABEL_ID", "LABEL_NAME", "COLOR")

  def apply(l: SyntaxProvider[Label])(rs: WrappedResultSet): Label = apply(l.resultName)(rs)
  def apply(l: ResultName[Label])(rs: WrappedResultSet): Label = new Label(
    userName = rs.get(l.userName),
    repositoryName = rs.get(l.repositoryName),
    labelId = rs.get(l.labelId),
    labelName = rs.get(l.labelName),
    color = rs.get(l.color)
  )

  val l = Label.syntax("l")

  override val autoSession = AutoSession

  def find(labelId: Int, repositoryName: String, userName: String)(implicit session: DBSession = autoSession): Option[Label] = {
    withSQL {
      select.from(Label as l).where.eq(l.labelId, labelId).and.eq(l.repositoryName, repositoryName).and.eq(l.userName, userName)
    }.map(Label(l.resultName)).single.apply()
  }

  def findAll()(implicit session: DBSession = autoSession): List[Label] = {
    withSQL(select.from(Label as l)).map(Label(l.resultName)).list.apply()
  }

  def countAll()(implicit session: DBSession = autoSession): Long = {
    withSQL(select(sqls.count).from(Label as l)).map(rs => rs.long(1)).single.apply().get
  }

  def findBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Option[Label] = {
    withSQL {
      select.from(Label as l).where.append(where)
    }.map(Label(l.resultName)).single.apply()
  }

  def findAllBy(where: SQLSyntax)(implicit session: DBSession = autoSession): List[Label] = {
    withSQL {
      select.from(Label as l).where.append(where)
    }.map(Label(l.resultName)).list.apply()
  }

  def countBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Long = {
    withSQL {
      select(sqls.count).from(Label as l).where.append(where)
    }.map(_.long(1)).single.apply().get
  }

  def create(
    userName: String,
    repositoryName: String,
    labelName: String,
    color: String)(implicit session: DBSession = autoSession): Label = {
    val generatedKey = withSQL {
      insert.into(Label).columns(
        column.userName,
        column.repositoryName,
        column.labelName,
        column.color
      ).values(
        userName,
        repositoryName,
        labelName,
        color
      )
    }.updateAndReturnGeneratedKey.apply()

    Label(
      labelId = generatedKey.toInt,
      userName = userName,
      repositoryName = repositoryName,
      labelName = labelName,
      color = color)
  }

  def save(entity: Label)(implicit session: DBSession = autoSession): Label = {
    withSQL {
      update(Label).set(
        column.userName -> entity.userName,
        column.repositoryName -> entity.repositoryName,
        column.labelId -> entity.labelId,
        column.labelName -> entity.labelName,
        column.color -> entity.color
      ).where.eq(column.labelId, entity.labelId).and.eq(column.repositoryName, entity.repositoryName).and.eq(column.userName, entity.userName)
    }.update.apply()
    entity
  }

  def destroy(entity: Label)(implicit session: DBSession = autoSession): Unit = {
    withSQL { delete.from(Label).where.eq(column.labelId, entity.labelId).and.eq(column.repositoryName, entity.repositoryName).and.eq(column.userName, entity.userName) }.update.apply()
  }

}
