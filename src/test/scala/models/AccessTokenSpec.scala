package models

import models.gitbucket.AccessToken
import scalikejdbc.specs2.mutable.AutoRollback
import org.specs2.mutable._
import scalikejdbc._


class AccessTokenSpec extends Specification {

  "AccessToken" should {

    val at = AccessToken.syntax("at")

    "find by primary keys" in new AutoRollback {
      val maybeFound = AccessToken.find(123)
      maybeFound.isDefined should beTrue
    }
    "find by where clauses" in new AutoRollback {
      val maybeFound = AccessToken.findBy(sqls.eq(at.accessTokenId, 123))
      maybeFound.isDefined should beTrue
    }
    "find all records" in new AutoRollback {
      val allResults = AccessToken.findAll()
      allResults.size should be_>(0)
    }
    "count all records" in new AutoRollback {
      val count = AccessToken.countAll()
      count should be_>(0L)
    }
    "find all by where clauses" in new AutoRollback {
      val results = AccessToken.findAllBy(sqls.eq(at.accessTokenId, 123))
      results.size should be_>(0)
    }
    "count by where clauses" in new AutoRollback {
      val count = AccessToken.countBy(sqls.eq(at.accessTokenId, 123))
      count should be_>(0L)
    }
    "create new record" in new AutoRollback {
      val created = AccessToken.create(tokenHash = "MyString", userName = "MyString", note = null)
      created should not beNull
    }
    "save a record" in new AutoRollback {
      val entity = AccessToken.findAll().head
      // TODO modify something
      val modified = entity
      val updated = AccessToken.save(modified)
      updated should not equalTo(entity)
    }
    "destroy a record" in new AutoRollback {
      val entity = AccessToken.findAll().head
      AccessToken.destroy(entity)
      val shouldBeNone = AccessToken.find(123)
      shouldBeNone.isDefined should beFalse
    }
  }

}
