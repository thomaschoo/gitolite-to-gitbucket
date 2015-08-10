package models.gitolite

import org.joda.time.DateTime
import org.specs2.mutable._

import scalikejdbc._
import scalikejdbc.specs2.mutable.AutoRollback

class WebHooksSpec extends Specification {

  "WebHooks" should {

    val wh = WebHooks.syntax("wh")

    "find by primary keys" in new AutoRollback {
      val maybeFound = WebHooks.find(123)
      maybeFound.isDefined should beTrue
    }
    "find by where clauses" in new AutoRollback {
      val maybeFound = WebHooks.findBy(sqls.eq(wh.id, 123))
      maybeFound.isDefined should beTrue
    }
    "find all records" in new AutoRollback {
      val allResults = WebHooks.findAll()
      allResults.size should be_>(0)
    }
    "count all records" in new AutoRollback {
      val count = WebHooks.countAll()
      count should be_>(0L)
    }
    "find all by where clauses" in new AutoRollback {
      val results = WebHooks.findAllBy(sqls.eq(wh.id, 123))
      results.size should be_>(0)
    }
    "count by where clauses" in new AutoRollback {
      val count = WebHooks.countBy(sqls.eq(wh.id, 123))
      count should be_>(0L)
    }
    "create new record" in new AutoRollback {
      val created = WebHooks.create(createdAt = DateTime.now, updatedAt = DateTime.now)
      created should not beNull
    }
    "save a record" in new AutoRollback {
      val entity = WebHooks.findAll().head
      // TODO modify something
      val modified = entity
      val updated = WebHooks.save(modified)
      updated should not equalTo(entity)
    }
    "destroy a record" in new AutoRollback {
      val entity = WebHooks.findAll().head
      WebHooks.destroy(entity)
      val shouldBeNone = WebHooks.find(123)
      shouldBeNone.isDefined should beFalse
    }
  }

}
