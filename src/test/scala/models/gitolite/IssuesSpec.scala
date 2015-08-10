package models.gitolite

import org.joda.time.DateTime
import org.specs2.mutable._

import scalikejdbc._


class IssuesSpec extends Specification {

  "Issues" should {

    val i = Issues.syntax("i")

    "find by primary keys" in new AutoRollback {
      val maybeFound = Issues.find(123)
      maybeFound.isDefined should beTrue
    }
    "find by where clauses" in new AutoRollback {
      val maybeFound = Issues.findBy(sqls.eq(i.id, 123))
      maybeFound.isDefined should beTrue
    }
    "find all records" in new AutoRollback {
      val allResults = Issues.findAll()
      allResults.size should be_>(0)
    }
    "count all records" in new AutoRollback {
      val count = Issues.countAll()
      count should be_>(0L)
    }
    "find all by where clauses" in new AutoRollback {
      val results = Issues.findAllBy(sqls.eq(i.id, 123))
      results.size should be_>(0)
    }
    "count by where clauses" in new AutoRollback {
      val count = Issues.countBy(sqls.eq(i.id, 123))
      count should be_>(0L)
    }
    "create new record" in new AutoRollback {
      val created = Issues.create(createdAt = DateTime.now, updatedAt = DateTime.now, closed = false)
      created should not beNull
    }
    "save a record" in new AutoRollback {
      val entity = Issues.findAll().head
      // TODO modify something
      val modified = entity
      val updated = Issues.save(modified)
      updated should not equalTo(entity)
    }
    "destroy a record" in new AutoRollback {
      val entity = Issues.findAll().head
      Issues.destroy(entity)
      val shouldBeNone = Issues.find(123)
      shouldBeNone.isDefined should beFalse
    }
  }

}
