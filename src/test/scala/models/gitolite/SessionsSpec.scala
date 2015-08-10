package models.gitolite

import org.joda.time.DateTime
import org.specs2.mutable._

import scalikejdbc._


class SessionsSpec extends Specification {

  "Sessions" should {

    val s = Sessions.syntax("s")

    "find by primary keys" in new AutoRollback {
      val maybeFound = Sessions.find(123)
      maybeFound.isDefined should beTrue
    }
    "find by where clauses" in new AutoRollback {
      val maybeFound = Sessions.findBy(sqls.eq(s.id, 123))
      maybeFound.isDefined should beTrue
    }
    "find all records" in new AutoRollback {
      val allResults = Sessions.findAll()
      allResults.size should be_>(0)
    }
    "count all records" in new AutoRollback {
      val count = Sessions.countAll()
      count should be_>(0L)
    }
    "find all by where clauses" in new AutoRollback {
      val results = Sessions.findAllBy(sqls.eq(s.id, 123))
      results.size should be_>(0)
    }
    "count by where clauses" in new AutoRollback {
      val count = Sessions.countBy(sqls.eq(s.id, 123))
      count should be_>(0L)
    }
    "create new record" in new AutoRollback {
      val created = Sessions.create(sessionId = "MyString", createdAt = DateTime.now, updatedAt = DateTime.now)
      created should not beNull
    }
    "save a record" in new AutoRollback {
      val entity = Sessions.findAll().head
      // TODO modify something
      val modified = entity
      val updated = Sessions.save(modified)
      updated should not equalTo(entity)
    }
    "destroy a record" in new AutoRollback {
      val entity = Sessions.findAll().head
      Sessions.destroy(entity)
      val shouldBeNone = Sessions.find(123)
      shouldBeNone.isDefined should beFalse
    }
  }

}
