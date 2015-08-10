package models.gitolite

import org.joda.time.DateTime
import org.specs2.mutable._

import scalikejdbc._
import scalikejdbc.specs2.mutable.AutoRollback

class KeysSpec extends Specification {

  "Keys" should {

    val k = Keys.syntax("k")

    "find by primary keys" in new AutoRollback {
      val maybeFound = Keys.find(123)
      maybeFound.isDefined should beTrue
    }
    "find by where clauses" in new AutoRollback {
      val maybeFound = Keys.findBy(sqls.eq(k.id, 123))
      maybeFound.isDefined should beTrue
    }
    "find all records" in new AutoRollback {
      val allResults = Keys.findAll()
      allResults.size should be_>(0)
    }
    "count all records" in new AutoRollback {
      val count = Keys.countAll()
      count should be_>(0L)
    }
    "find all by where clauses" in new AutoRollback {
      val results = Keys.findAllBy(sqls.eq(k.id, 123))
      results.size should be_>(0)
    }
    "count by where clauses" in new AutoRollback {
      val count = Keys.countBy(sqls.eq(k.id, 123))
      count should be_>(0L)
    }
    "create new record" in new AutoRollback {
      val created = Keys.create(createdAt = DateTime.now, updatedAt = DateTime.now)
      created should not beNull
    }
    "save a record" in new AutoRollback {
      val entity = Keys.findAll().head
      // TODO modify something
      val modified = entity
      val updated = Keys.save(modified)
      updated should not equalTo(entity)
    }
    "destroy a record" in new AutoRollback {
      val entity = Keys.findAll().head
      Keys.destroy(entity)
      val shouldBeNone = Keys.find(123)
      shouldBeNone.isDefined should beFalse
    }
  }

}
