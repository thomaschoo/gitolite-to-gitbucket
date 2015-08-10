package models.gitolite

import org.joda.time.DateTime
import org.specs2.mutable._

import scalikejdbc._
import scalikejdbc.specs2.mutable.AutoRollback

class SnippetsSpec extends Specification {

  "Snippets" should {

    val s = Snippets.syntax("s")

    "find by primary keys" in new AutoRollback {
      val maybeFound = Snippets.find(123)
      maybeFound.isDefined should beTrue
    }
    "find by where clauses" in new AutoRollback {
      val maybeFound = Snippets.findBy(sqls.eq(s.id, 123))
      maybeFound.isDefined should beTrue
    }
    "find all records" in new AutoRollback {
      val allResults = Snippets.findAll()
      allResults.size should be_>(0)
    }
    "count all records" in new AutoRollback {
      val count = Snippets.countAll()
      count should be_>(0L)
    }
    "find all by where clauses" in new AutoRollback {
      val results = Snippets.findAllBy(sqls.eq(s.id, 123))
      results.size should be_>(0)
    }
    "count by where clauses" in new AutoRollback {
      val count = Snippets.countBy(sqls.eq(s.id, 123))
      count should be_>(0L)
    }
    "create new record" in new AutoRollback {
      val created = Snippets.create(authorId = 123, projectId = 123, createdAt = DateTime.now, updatedAt = DateTime.now)
      created should not beNull
    }
    "save a record" in new AutoRollback {
      val entity = Snippets.findAll().head
      // TODO modify something
      val modified = entity
      val updated = Snippets.save(modified)
      updated should not equalTo(entity)
    }
    "destroy a record" in new AutoRollback {
      val entity = Snippets.findAll().head
      Snippets.destroy(entity)
      val shouldBeNone = Snippets.find(123)
      shouldBeNone.isDefined should beFalse
    }
  }

}
