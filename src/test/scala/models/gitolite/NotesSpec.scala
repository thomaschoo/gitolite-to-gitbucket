package models.gitolite

import org.joda.time.DateTime
import org.specs2.mutable._

import scalikejdbc._
import scalikejdbc.specs2.mutable.AutoRollback

class NotesSpec extends Specification {

  "Notes" should {

    val n = Notes.syntax("n")

    "find by primary keys" in new AutoRollback {
      val maybeFound = Notes.find(123)
      maybeFound.isDefined should beTrue
    }
    "find by where clauses" in new AutoRollback {
      val maybeFound = Notes.findBy(sqls.eq(n.id, 123))
      maybeFound.isDefined should beTrue
    }
    "find all records" in new AutoRollback {
      val allResults = Notes.findAll()
      allResults.size should be_>(0)
    }
    "count all records" in new AutoRollback {
      val count = Notes.countAll()
      count should be_>(0L)
    }
    "find all by where clauses" in new AutoRollback {
      val results = Notes.findAllBy(sqls.eq(n.id, 123))
      results.size should be_>(0)
    }
    "count by where clauses" in new AutoRollback {
      val count = Notes.countBy(sqls.eq(n.id, 123))
      count should be_>(0L)
    }
    "create new record" in new AutoRollback {
      val created = Notes.create(createdAt = DateTime.now, updatedAt = DateTime.now)
      created should not beNull
    }
    "save a record" in new AutoRollback {
      val entity = Notes.findAll().head
      // TODO modify something
      val modified = entity
      val updated = Notes.save(modified)
      updated should not equalTo(entity)
    }
    "destroy a record" in new AutoRollback {
      val entity = Notes.findAll().head
      Notes.destroy(entity)
      val shouldBeNone = Notes.find(123)
      shouldBeNone.isDefined should beFalse
    }
  }

}
