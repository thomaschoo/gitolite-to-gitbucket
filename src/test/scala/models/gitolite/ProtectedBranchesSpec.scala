package models.gitolite

import org.joda.time.DateTime
import org.specs2.mutable._

import scalikejdbc._
import scalikejdbc.specs2.mutable.AutoRollback

class ProtectedBranchesSpec extends Specification {

  "ProtectedBranches" should {

    val pb = ProtectedBranches.syntax("pb")

    "find by primary keys" in new AutoRollback {
      val maybeFound = ProtectedBranches.find(123)
      maybeFound.isDefined should beTrue
    }
    "find by where clauses" in new AutoRollback {
      val maybeFound = ProtectedBranches.findBy(sqls.eq(pb.id, 123))
      maybeFound.isDefined should beTrue
    }
    "find all records" in new AutoRollback {
      val allResults = ProtectedBranches.findAll()
      allResults.size should be_>(0)
    }
    "count all records" in new AutoRollback {
      val count = ProtectedBranches.countAll()
      count should be_>(0L)
    }
    "find all by where clauses" in new AutoRollback {
      val results = ProtectedBranches.findAllBy(sqls.eq(pb.id, 123))
      results.size should be_>(0)
    }
    "count by where clauses" in new AutoRollback {
      val count = ProtectedBranches.countBy(sqls.eq(pb.id, 123))
      count should be_>(0L)
    }
    "create new record" in new AutoRollback {
      val created = ProtectedBranches.create(projectId = 123, name = "MyString", createdAt = DateTime.now, updatedAt = DateTime.now)
      created should not beNull
    }
    "save a record" in new AutoRollback {
      val entity = ProtectedBranches.findAll().head
      // TODO modify something
      val modified = entity
      val updated = ProtectedBranches.save(modified)
      updated should not equalTo(entity)
    }
    "destroy a record" in new AutoRollback {
      val entity = ProtectedBranches.findAll().head
      ProtectedBranches.destroy(entity)
      val shouldBeNone = ProtectedBranches.find(123)
      shouldBeNone.isDefined should beFalse
    }
  }

}
