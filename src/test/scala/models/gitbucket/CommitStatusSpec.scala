package models.gitbucket

import org.joda.time.DateTime
import org.specs2.mutable._
import scalikejdbc._
import scalikejdbc.specs2.mutable.AutoRollback

class CommitStatusSpec extends Specification {

  "CommitStatus" should {

    val cs = CommitStatus.syntax("cs")

    "find by primary keys" in new AutoRollback {
      val maybeFound = CommitStatus.find(123)
      maybeFound.isDefined should beTrue
    }
    "find by where clauses" in new AutoRollback {
      val maybeFound = CommitStatus.findBy(sqls.eq(cs.commitStatusId, 123))
      maybeFound.isDefined should beTrue
    }
    "find all records" in new AutoRollback {
      val allResults = CommitStatus.findAll()
      allResults.size should be_>(0)
    }
    "count all records" in new AutoRollback {
      val count = CommitStatus.countAll()
      count should be_>(0L)
    }
    "find all by where clauses" in new AutoRollback {
      val results = CommitStatus.findAllBy(sqls.eq(cs.commitStatusId, 123))
      results.size should be_>(0)
    }
    "count by where clauses" in new AutoRollback {
      val count = CommitStatus.countBy(sqls.eq(cs.commitStatusId, 123))
      count should be_>(0L)
    }
    "create new record" in new AutoRollback {
      val created = CommitStatus.create(userName = "MyString", repositoryName = "MyString", commitId = "MyString", context = "MyString", state = "MyString", creator = "MyString", registeredDate = DateTime.now, updatedDate = DateTime.now)
      created should not beNull
    }
    "save a record" in new AutoRollback {
      val entity = CommitStatus.findAll().head
      // TODO modify something
      val modified = entity
      val updated = CommitStatus.save(modified)
      updated should not equalTo(entity)
    }
    "destroy a record" in new AutoRollback {
      val entity = CommitStatus.findAll().head
      CommitStatus.destroy(entity)
      val shouldBeNone = CommitStatus.find(123)
      shouldBeNone.isDefined should beFalse
    }
  }

}
