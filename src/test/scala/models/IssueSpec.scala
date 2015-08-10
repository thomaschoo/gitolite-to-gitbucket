package models

import models.gitbucket.Issue
import scalikejdbc.specs2.mutable.AutoRollback
import org.specs2.mutable._
import scalikejdbc._
import org.joda.time.{DateTime}


class IssueSpec extends Specification {

  "Issue" should {

    val i = Issue.syntax("i")

    "find by primary keys" in new AutoRollback {
      val maybeFound = Issue.find(123, "MyString", "MyString")
      maybeFound.isDefined should beTrue
    }
    "find by where clauses" in new AutoRollback {
      val maybeFound = Issue.findBy(sqls.eq(i.issueId, 123))
      maybeFound.isDefined should beTrue
    }
    "find all records" in new AutoRollback {
      val allResults = Issue.findAll()
      allResults.size should be_>(0)
    }
    "count all records" in new AutoRollback {
      val count = Issue.countAll()
      count should be_>(0L)
    }
    "find all by where clauses" in new AutoRollback {
      val results = Issue.findAllBy(sqls.eq(i.issueId, 123))
      results.size should be_>(0)
    }
    "count by where clauses" in new AutoRollback {
      val count = Issue.countBy(sqls.eq(i.issueId, 123))
      count should be_>(0L)
    }
    "create new record" in new AutoRollback {
      val created = Issue.create(userName = "MyString", repositoryName = "MyString", issueId = 123, openedUserName = "MyString", title = null, closed = false, registeredDate = DateTime.now, updatedDate = DateTime.now, pullRequest = false)
      created should not beNull
    }
    "save a record" in new AutoRollback {
      val entity = Issue.findAll().head
      // TODO modify something
      val modified = entity
      val updated = Issue.save(modified)
      updated should not equalTo(entity)
    }
    "destroy a record" in new AutoRollback {
      val entity = Issue.findAll().head
      Issue.destroy(entity)
      val shouldBeNone = Issue.find(123, "MyString", "MyString")
      shouldBeNone.isDefined should beFalse
    }
  }

}
