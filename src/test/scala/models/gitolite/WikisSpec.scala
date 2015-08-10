package models.gitolite

import org.joda.time.DateTime
import org.specs2.mutable._

import scalikejdbc._


class WikisSpec extends Specification {

  "Wikis" should {

    val w = Wikis.syntax("w")

    "find by primary keys" in new AutoRollback {
      val maybeFound = Wikis.find(123)
      maybeFound.isDefined should beTrue
    }
    "find by where clauses" in new AutoRollback {
      val maybeFound = Wikis.findBy(sqls.eq(w.id, 123))
      maybeFound.isDefined should beTrue
    }
    "find all records" in new AutoRollback {
      val allResults = Wikis.findAll()
      allResults.size should be_>(0)
    }
    "count all records" in new AutoRollback {
      val count = Wikis.countAll()
      count should be_>(0L)
    }
    "find all by where clauses" in new AutoRollback {
      val results = Wikis.findAllBy(sqls.eq(w.id, 123))
      results.size should be_>(0)
    }
    "count by where clauses" in new AutoRollback {
      val count = Wikis.countBy(sqls.eq(w.id, 123))
      count should be_>(0L)
    }
    "create new record" in new AutoRollback {
      val created = Wikis.create(createdAt = DateTime.now, updatedAt = DateTime.now)
      created should not beNull
    }
    "save a record" in new AutoRollback {
      val entity = Wikis.findAll().head
      // TODO modify something
      val modified = entity
      val updated = Wikis.save(modified)
      updated should not equalTo(entity)
    }
    "destroy a record" in new AutoRollback {
      val entity = Wikis.findAll().head
      Wikis.destroy(entity)
      val shouldBeNone = Wikis.find(123)
      shouldBeNone.isDefined should beFalse
    }
  }

}
