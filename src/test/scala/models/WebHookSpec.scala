package models

import models.gitbucket.WebHook
import scalikejdbc.specs2.mutable.AutoRollback
import org.specs2.mutable._
import scalikejdbc._


class WebHookSpec extends Specification {

  "WebHook" should {

    val wh = WebHook.syntax("wh")

    "find by primary keys" in new AutoRollback {
      val maybeFound = WebHook.find("MyString", "MyString", "MyString")
      maybeFound.isDefined should beTrue
    }
    "find by where clauses" in new AutoRollback {
      val maybeFound = WebHook.findBy(sqls.eq(wh.repositoryName, "MyString"))
      maybeFound.isDefined should beTrue
    }
    "find all records" in new AutoRollback {
      val allResults = WebHook.findAll()
      allResults.size should be_>(0)
    }
    "count all records" in new AutoRollback {
      val count = WebHook.countAll()
      count should be_>(0L)
    }
    "find all by where clauses" in new AutoRollback {
      val results = WebHook.findAllBy(sqls.eq(wh.repositoryName, "MyString"))
      results.size should be_>(0)
    }
    "count by where clauses" in new AutoRollback {
      val count = WebHook.countBy(sqls.eq(wh.repositoryName, "MyString"))
      count should be_>(0L)
    }
    "create new record" in new AutoRollback {
      val created = WebHook.create(userName = "MyString", repositoryName = "MyString", url = "MyString")
      created should not beNull
    }
    "save a record" in new AutoRollback {
      val entity = WebHook.findAll().head
      // TODO modify something
      val modified = entity
      val updated = WebHook.save(modified)
      updated should not equalTo(entity)
    }
    "destroy a record" in new AutoRollback {
      val entity = WebHook.findAll().head
      WebHook.destroy(entity)
      val shouldBeNone = WebHook.find("MyString", "MyString", "MyString")
      shouldBeNone.isDefined should beFalse
    }
  }

}
