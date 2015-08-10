package models

import models.gitbucket.Milestone
import scalikejdbc.specs2.mutable.AutoRollback
import org.specs2.mutable._
import scalikejdbc._
import org.joda.time.{DateTime}


class MilestoneSpec extends Specification {

  "Milestone" should {

    val m = Milestone.syntax("m")

    "find by primary keys" in new AutoRollback {
      val maybeFound = Milestone.find(123, "MyString", "MyString")
      maybeFound.isDefined should beTrue
    }
    "find by where clauses" in new AutoRollback {
      val maybeFound = Milestone.findBy(sqls.eq(m.milestoneId, 123))
      maybeFound.isDefined should beTrue
    }
    "find all records" in new AutoRollback {
      val allResults = Milestone.findAll()
      allResults.size should be_>(0)
    }
    "count all records" in new AutoRollback {
      val count = Milestone.countAll()
      count should be_>(0L)
    }
    "find all by where clauses" in new AutoRollback {
      val results = Milestone.findAllBy(sqls.eq(m.milestoneId, 123))
      results.size should be_>(0)
    }
    "count by where clauses" in new AutoRollback {
      val count = Milestone.countBy(sqls.eq(m.milestoneId, 123))
      count should be_>(0L)
    }
    "create new record" in new AutoRollback {
      val created = Milestone.create(userName = "MyString", repositoryName = "MyString", title = "MyString")
      created should not beNull
    }
    "save a record" in new AutoRollback {
      val entity = Milestone.findAll().head
      // TODO modify something
      val modified = entity
      val updated = Milestone.save(modified)
      updated should not equalTo(entity)
    }
    "destroy a record" in new AutoRollback {
      val entity = Milestone.findAll().head
      Milestone.destroy(entity)
      val shouldBeNone = Milestone.find(123, "MyString", "MyString")
      shouldBeNone.isDefined should beFalse
    }
  }

}
