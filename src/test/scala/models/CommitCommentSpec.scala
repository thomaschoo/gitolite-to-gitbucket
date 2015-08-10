package models

import models.gitbucket.CommitComment
import scalikejdbc.specs2.mutable.AutoRollback
import org.specs2.mutable._
import scalikejdbc._
import org.joda.time.{DateTime}


class CommitCommentSpec extends Specification {

  "CommitComment" should {

    val cc = CommitComment.syntax("cc")

    "find by primary keys" in new AutoRollback {
      val maybeFound = CommitComment.find(123)
      maybeFound.isDefined should beTrue
    }
    "find by where clauses" in new AutoRollback {
      val maybeFound = CommitComment.findBy(sqls.eq(cc.commentId, 123))
      maybeFound.isDefined should beTrue
    }
    "find all records" in new AutoRollback {
      val allResults = CommitComment.findAll()
      allResults.size should be_>(0)
    }
    "count all records" in new AutoRollback {
      val count = CommitComment.countAll()
      count should be_>(0L)
    }
    "find all by where clauses" in new AutoRollback {
      val results = CommitComment.findAllBy(sqls.eq(cc.commentId, 123))
      results.size should be_>(0)
    }
    "count by where clauses" in new AutoRollback {
      val count = CommitComment.countBy(sqls.eq(cc.commentId, 123))
      count should be_>(0L)
    }
    "create new record" in new AutoRollback {
      val created = CommitComment.create(userName = "MyString", repositoryName = "MyString", commitId = "MyString", commentedUserName = "MyString", content = null, registeredDate = DateTime.now, updatedDate = DateTime.now, pullRequest = false)
      created should not beNull
    }
    "save a record" in new AutoRollback {
      val entity = CommitComment.findAll().head
      // TODO modify something
      val modified = entity
      val updated = CommitComment.save(modified)
      updated should not equalTo(entity)
    }
    "destroy a record" in new AutoRollback {
      val entity = CommitComment.findAll().head
      CommitComment.destroy(entity)
      val shouldBeNone = CommitComment.find(123)
      shouldBeNone.isDefined should beFalse
    }
  }

}
