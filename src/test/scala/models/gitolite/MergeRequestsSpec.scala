package models.gitolite

import org.joda.time.DateTime
import org.specs2.mutable._

import scalikejdbc._


class MergeRequestsSpec extends Specification {

  "MergeRequests" should {

    val mr = MergeRequests.syntax("mr")

    "find by primary keys" in new AutoRollback {
      val maybeFound = MergeRequests.find(123)
      maybeFound.isDefined should beTrue
    }
    "find by where clauses" in new AutoRollback {
      val maybeFound = MergeRequests.findBy(sqls.eq(mr.id, 123))
      maybeFound.isDefined should beTrue
    }
    "find all records" in new AutoRollback {
      val allResults = MergeRequests.findAll()
      allResults.size should be_>(0)
    }
    "count all records" in new AutoRollback {
      val count = MergeRequests.countAll()
      count should be_>(0L)
    }
    "find all by where clauses" in new AutoRollback {
      val results = MergeRequests.findAllBy(sqls.eq(mr.id, 123))
      results.size should be_>(0)
    }
    "count by where clauses" in new AutoRollback {
      val count = MergeRequests.countBy(sqls.eq(mr.id, 123))
      count should be_>(0L)
    }
    "create new record" in new AutoRollback {
      val created = MergeRequests.create(targetBranch = "MyString", sourceBranch = "MyString", projectId = 123, closed = false, createdAt = DateTime.now, updatedAt = DateTime.now, merged = false, state = 123)
      created should not beNull
    }
    "save a record" in new AutoRollback {
      val entity = MergeRequests.findAll().head
      // TODO modify something
      val modified = entity
      val updated = MergeRequests.save(modified)
      updated should not equalTo(entity)
    }
    "destroy a record" in new AutoRollback {
      val entity = MergeRequests.findAll().head
      MergeRequests.destroy(entity)
      val shouldBeNone = MergeRequests.find(123)
      shouldBeNone.isDefined should beFalse
    }
  }

}
