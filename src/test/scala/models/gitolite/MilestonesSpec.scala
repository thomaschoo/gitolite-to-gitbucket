package models.gitolite

import org.joda.time.DateTime
import org.specs2.mutable._

import scalikejdbc._


class MilestonesSpec extends Specification {

  "Milestones" should {

    val m = Milestones.syntax("m")

    "find by primary keys" in new AutoRollback {
      val maybeFound = Milestones.find(123)
      maybeFound.isDefined should beTrue
    }
    "find by where clauses" in new AutoRollback {
      val maybeFound = Milestones.findBy(sqls.eq(m.id, 123))
      maybeFound.isDefined should beTrue
    }
    "find all records" in new AutoRollback {
      val allResults = Milestones.findAll()
      allResults.size should be_>(0)
    }
    "count all records" in new AutoRollback {
      val count = Milestones.countAll()
      count should be_>(0L)
    }
    "find all by where clauses" in new AutoRollback {
      val results = Milestones.findAllBy(sqls.eq(m.id, 123))
      results.size should be_>(0)
    }
    "count by where clauses" in new AutoRollback {
      val count = Milestones.countBy(sqls.eq(m.id, 123))
      count should be_>(0L)
    }
    "create new record" in new AutoRollback {
      val created = Milestones.create(title = "MyString", projectId = 123, closed = false, createdAt = DateTime.now, updatedAt = DateTime.now)
      created should not beNull
    }
    "save a record" in new AutoRollback {
      val entity = Milestones.findAll().head
      // TODO modify something
      val modified = entity
      val updated = Milestones.save(modified)
      updated should not equalTo(entity)
    }
    "destroy a record" in new AutoRollback {
      val entity = Milestones.findAll().head
      Milestones.destroy(entity)
      val shouldBeNone = Milestones.find(123)
      shouldBeNone.isDefined should beFalse
    }
  }

}
