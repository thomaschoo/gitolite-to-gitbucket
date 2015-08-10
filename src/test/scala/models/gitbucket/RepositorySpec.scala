package models.gitbucket

import org.joda.time.DateTime
import org.specs2.mutable._
import scalikejdbc._
import scalikejdbc.specs2.mutable.AutoRollback

class RepositorySpec extends Specification {

  "Repository" should {

    val r = Repository.syntax("r")

    "find by primary keys" in new AutoRollback {
      val maybeFound = Repository.find("MyString", "MyString")
      maybeFound.isDefined should beTrue
    }
    "find by where clauses" in new AutoRollback {
      val maybeFound = Repository.findBy(sqls.eq(r.repositoryName, "MyString"))
      maybeFound.isDefined should beTrue
    }
    "find all records" in new AutoRollback {
      val allResults = Repository.findAll()
      allResults.size should be_>(0)
    }
    "count all records" in new AutoRollback {
      val count = Repository.countAll()
      count should be_>(0L)
    }
    "find all by where clauses" in new AutoRollback {
      val results = Repository.findAllBy(sqls.eq(r.repositoryName, "MyString"))
      results.size should be_>(0)
    }
    "count by where clauses" in new AutoRollback {
      val count = Repository.countBy(sqls.eq(r.repositoryName, "MyString"))
      count should be_>(0L)
    }
    "create new record" in new AutoRollback {
      val created = Repository.create(userName = "MyString", repositoryName = "MyString", `private` = false, registeredDate = DateTime.now, updatedDate = DateTime.now, lastActivityDate = DateTime.now)
      created should not beNull
    }
    "save a record" in new AutoRollback {
      val entity = Repository.findAll().head
      // TODO modify something
      val modified = entity
      val updated = Repository.save(modified)
      updated should not equalTo(entity)
    }
    "destroy a record" in new AutoRollback {
      val entity = Repository.findAll().head
      Repository.destroy(entity)
      val shouldBeNone = Repository.find("MyString", "MyString")
      shouldBeNone.isDefined should beFalse
    }
  }

}
