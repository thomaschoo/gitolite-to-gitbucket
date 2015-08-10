package models

import models.gitbucket.PullRequest
import scalikejdbc.specs2.mutable.AutoRollback
import org.specs2.mutable._
import scalikejdbc._


class PullRequestSpec extends Specification {

  "PullRequest" should {

    val pr = PullRequest.syntax("pr")

    "find by primary keys" in new AutoRollback {
      val maybeFound = PullRequest.find(123, "MyString", "MyString")
      maybeFound.isDefined should beTrue
    }
    "find by where clauses" in new AutoRollback {
      val maybeFound = PullRequest.findBy(sqls.eq(pr.issueId, 123))
      maybeFound.isDefined should beTrue
    }
    "find all records" in new AutoRollback {
      val allResults = PullRequest.findAll()
      allResults.size should be_>(0)
    }
    "count all records" in new AutoRollback {
      val count = PullRequest.countAll()
      count should be_>(0L)
    }
    "find all by where clauses" in new AutoRollback {
      val results = PullRequest.findAllBy(sqls.eq(pr.issueId, 123))
      results.size should be_>(0)
    }
    "count by where clauses" in new AutoRollback {
      val count = PullRequest.countBy(sqls.eq(pr.issueId, 123))
      count should be_>(0L)
    }
    "create new record" in new AutoRollback {
      val created = PullRequest.create(userName = "MyString", repositoryName = "MyString", issueId = 123, branch = "MyString", requestUserName = "MyString", requestRepositoryName = "MyString", requestBranch = "MyString", commitIdFrom = "MyString", commitIdTo = "MyString")
      created should not beNull
    }
    "save a record" in new AutoRollback {
      val entity = PullRequest.findAll().head
      // TODO modify something
      val modified = entity
      val updated = PullRequest.save(modified)
      updated should not equalTo(entity)
    }
    "destroy a record" in new AutoRollback {
      val entity = PullRequest.findAll().head
      PullRequest.destroy(entity)
      val shouldBeNone = PullRequest.find(123, "MyString", "MyString")
      shouldBeNone.isDefined should beFalse
    }
  }

}
