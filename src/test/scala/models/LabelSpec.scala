package models

import models.gitbucket.Label
import scalikejdbc.specs2.mutable.AutoRollback
import org.specs2.mutable._
import scalikejdbc._


class LabelSpec extends Specification {

  "Label" should {

    val l = Label.syntax("l")

    "find by primary keys" in new AutoRollback {
      val maybeFound = Label.find(123, "MyString", "MyString")
      maybeFound.isDefined should beTrue
    }
    "find by where clauses" in new AutoRollback {
      val maybeFound = Label.findBy(sqls.eq(l.labelId, 123))
      maybeFound.isDefined should beTrue
    }
    "find all records" in new AutoRollback {
      val allResults = Label.findAll()
      allResults.size should be_>(0)
    }
    "count all records" in new AutoRollback {
      val count = Label.countAll()
      count should be_>(0L)
    }
    "find all by where clauses" in new AutoRollback {
      val results = Label.findAllBy(sqls.eq(l.labelId, 123))
      results.size should be_>(0)
    }
    "count by where clauses" in new AutoRollback {
      val count = Label.countBy(sqls.eq(l.labelId, 123))
      count should be_>(0L)
    }
    "create new record" in new AutoRollback {
      val created = Label.create(userName = "MyString", repositoryName = "MyString", labelName = "MyString", color = "MyString")
      created should not beNull
    }
    "save a record" in new AutoRollback {
      val entity = Label.findAll().head
      // TODO modify something
      val modified = entity
      val updated = Label.save(modified)
      updated should not equalTo(entity)
    }
    "destroy a record" in new AutoRollback {
      val entity = Label.findAll().head
      Label.destroy(entity)
      val shouldBeNone = Label.find(123, "MyString", "MyString")
      shouldBeNone.isDefined should beFalse
    }
  }

}
