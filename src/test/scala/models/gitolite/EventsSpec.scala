package models.gitolite

import org.joda.time.DateTime
import org.specs2.mutable._

import scalikejdbc._
import scalikejdbc.specs2.mutable.AutoRollback

class EventsSpec extends Specification {

  "Events" should {

    val e = Events.syntax("e")

    "find by primary keys" in new AutoRollback {
      val maybeFound = Events.find(123)
      maybeFound.isDefined should beTrue
    }
    "find by where clauses" in new AutoRollback {
      val maybeFound = Events.findBy(sqls.eq(e.id, 123))
      maybeFound.isDefined should beTrue
    }
    "find all records" in new AutoRollback {
      val allResults = Events.findAll()
      allResults.size should be_>(0)
    }
    "count all records" in new AutoRollback {
      val count = Events.countAll()
      count should be_>(0L)
    }
    "find all by where clauses" in new AutoRollback {
      val results = Events.findAllBy(sqls.eq(e.id, 123))
      results.size should be_>(0)
    }
    "count by where clauses" in new AutoRollback {
      val count = Events.countBy(sqls.eq(e.id, 123))
      count should be_>(0L)
    }
    "create new record" in new AutoRollback {
      val created = Events.create(createdAt = DateTime.now, updatedAt = DateTime.now)
      created should not beNull
    }
    "save a record" in new AutoRollback {
      val entity = Events.findAll().head
      // TODO modify something
      val modified = entity
      val updated = Events.save(modified)
      updated should not equalTo(entity)
    }
    "destroy a record" in new AutoRollback {
      val entity = Events.findAll().head
      Events.destroy(entity)
      val shouldBeNone = Events.find(123)
      shouldBeNone.isDefined should beFalse
    }
  }

}
