package models.gitbucket

import org.joda.time.DateTime
import org.specs2.mutable._
import scalikejdbc._
import scalikejdbc.specs2.mutable.AutoRollback

class IssueCommentSpec extends Specification {

  "IssueComment" should {

    val ic = IssueComment.syntax("ic")

    "find by primary keys" in new AutoRollback {
      val maybeFound = IssueComment.find(123)
      maybeFound.isDefined should beTrue
    }
    "find by where clauses" in new AutoRollback {
      val maybeFound = IssueComment.findBy(sqls.eq(ic.commentId, 123))
      maybeFound.isDefined should beTrue
    }
    "find all records" in new AutoRollback {
      val allResults = IssueComment.findAll()
      allResults.size should be_>(0)
    }
    "count all records" in new AutoRollback {
      val count = IssueComment.countAll()
      count should be_>(0L)
    }
    "find all by where clauses" in new AutoRollback {
      val results = IssueComment.findAllBy(sqls.eq(ic.commentId, 123))
      results.size should be_>(0)
    }
    "count by where clauses" in new AutoRollback {
      val count = IssueComment.countBy(sqls.eq(ic.commentId, 123))
      count should be_>(0L)
    }
    "create new record" in new AutoRollback {
      val created = IssueComment.create(userName = "MyString", repositoryName = "MyString", issueId = 123, action = "MyString", commentedUserName = "MyString", content = null, registeredDate = DateTime.now, updatedDate = DateTime.now)
      created should not beNull
    }
    "save a record" in new AutoRollback {
      val entity = IssueComment.findAll().head
      // TODO modify something
      val modified = entity
      val updated = IssueComment.save(modified)
      updated should not equalTo(entity)
    }
    "destroy a record" in new AutoRollback {
      val entity = IssueComment.findAll().head
      IssueComment.destroy(entity)
      val shouldBeNone = IssueComment.find(123)
      shouldBeNone.isDefined should beFalse
    }
  }

}
