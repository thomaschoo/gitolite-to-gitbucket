package models

import models.gitbucket.IssueLabel
import scalikejdbc.specs2.mutable.AutoRollback
import org.specs2.mutable._
import scalikejdbc._


class IssueLabelSpec extends Specification {

  "IssueLabel" should {

    val il = IssueLabel.syntax("il")

    "find by primary keys" in new AutoRollback {
      val maybeFound = IssueLabel.find(123, 123, "MyString", "MyString")
      maybeFound.isDefined should beTrue
    }
    "find by where clauses" in new AutoRollback {
      val maybeFound = IssueLabel.findBy(sqls.eq(il.issueId, 123))
      maybeFound.isDefined should beTrue
    }
    "find all records" in new AutoRollback {
      val allResults = IssueLabel.findAll()
      allResults.size should be_>(0)
    }
    "count all records" in new AutoRollback {
      val count = IssueLabel.countAll()
      count should be_>(0L)
    }
    "find all by where clauses" in new AutoRollback {
      val results = IssueLabel.findAllBy(sqls.eq(il.issueId, 123))
      results.size should be_>(0)
    }
    "count by where clauses" in new AutoRollback {
      val count = IssueLabel.countBy(sqls.eq(il.issueId, 123))
      count should be_>(0L)
    }
    "create new record" in new AutoRollback {
      val created = IssueLabel.create(userName = "MyString", repositoryName = "MyString", issueId = 123, labelId = 123)
      created should not beNull
    }
    "save a record" in new AutoRollback {
      val entity = IssueLabel.findAll().head
      // TODO modify something
      val modified = entity
      val updated = IssueLabel.save(modified)
      updated should not equalTo(entity)
    }
    "destroy a record" in new AutoRollback {
      val entity = IssueLabel.findAll().head
      IssueLabel.destroy(entity)
      val shouldBeNone = IssueLabel.find(123, 123, "MyString", "MyString")
      shouldBeNone.isDefined should beFalse
    }
  }

}
