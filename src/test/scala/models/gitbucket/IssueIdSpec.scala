package models.gitbucket

import org.specs2.mutable._
import scalikejdbc._
import scalikejdbc.specs2.mutable.AutoRollback

class IssueIdSpec extends Specification {

  "IssueId" should {

    val ii = IssueId.syntax("ii")

    "find by primary keys" in new AutoRollback {
      val maybeFound = IssueId.find("MyString", "MyString")
      maybeFound.isDefined should beTrue
    }
    "find by where clauses" in new AutoRollback {
      val maybeFound = IssueId.findBy(sqls.eq(ii.repositoryName, "MyString"))
      maybeFound.isDefined should beTrue
    }
    "find all records" in new AutoRollback {
      val allResults = IssueId.findAll()
      allResults.size should be_>(0)
    }
    "count all records" in new AutoRollback {
      val count = IssueId.countAll()
      count should be_>(0L)
    }
    "find all by where clauses" in new AutoRollback {
      val results = IssueId.findAllBy(sqls.eq(ii.repositoryName, "MyString"))
      results.size should be_>(0)
    }
    "count by where clauses" in new AutoRollback {
      val count = IssueId.countBy(sqls.eq(ii.repositoryName, "MyString"))
      count should be_>(0L)
    }
    "create new record" in new AutoRollback {
      val created = IssueId.create(userName = "MyString", repositoryName = "MyString", issueId = 123)
      created should not beNull
    }
    "save a record" in new AutoRollback {
      val entity = IssueId.findAll().head
      // TODO modify something
      val modified = entity
      val updated = IssueId.save(modified)
      updated should not equalTo(entity)
    }
    "destroy a record" in new AutoRollback {
      val entity = IssueId.findAll().head
      IssueId.destroy(entity)
      val shouldBeNone = IssueId.find("MyString", "MyString")
      shouldBeNone.isDefined should beFalse
    }
  }

}
