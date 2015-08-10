package models.gitolite

import org.specs2.mutable._

import scalikejdbc._


class TaggingsSpec extends Specification {

  "Taggings" should {

    val t = Taggings.syntax("t")

    "find by primary keys" in new AutoRollback {
      val maybeFound = Taggings.find(123)
      maybeFound.isDefined should beTrue
    }
    "find by where clauses" in new AutoRollback {
      val maybeFound = Taggings.findBy(sqls.eq(t.id, 123))
      maybeFound.isDefined should beTrue
    }
    "find all records" in new AutoRollback {
      val allResults = Taggings.findAll()
      allResults.size should be_>(0)
    }
    "count all records" in new AutoRollback {
      val count = Taggings.countAll()
      count should be_>(0L)
    }
    "find all by where clauses" in new AutoRollback {
      val results = Taggings.findAllBy(sqls.eq(t.id, 123))
      results.size should be_>(0)
    }
    "count by where clauses" in new AutoRollback {
      val count = Taggings.countBy(sqls.eq(t.id, 123))
      count should be_>(0L)
    }
    "create new record" in new AutoRollback {
      val created = Taggings.create()
      created should not beNull
    }
    "save a record" in new AutoRollback {
      val entity = Taggings.findAll().head
      // TODO modify something
      val modified = entity
      val updated = Taggings.save(modified)
      updated should not equalTo(entity)
    }
    "destroy a record" in new AutoRollback {
      val entity = Taggings.findAll().head
      Taggings.destroy(entity)
      val shouldBeNone = Taggings.find(123)
      shouldBeNone.isDefined should beFalse
    }
  }

}
