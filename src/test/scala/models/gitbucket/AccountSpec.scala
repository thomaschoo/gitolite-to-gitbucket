package models.gitbucket

import org.joda.time.DateTime
import org.specs2.mutable._
import scalikejdbc._
import scalikejdbc.specs2.mutable.AutoRollback


class AccountSpec extends Specification {

  "Account" should {

    val a = Account.syntax("a")

    "find by primary keys" in new AutoRollback {
      val maybeFound = Account.find("MyString")
      maybeFound.isDefined should beTrue
    }
    "find by where clauses" in new AutoRollback {
      val maybeFound = Account.findBy(sqls.eq(a.userName, "MyString"))
      maybeFound.isDefined should beTrue
    }
    "find all records" in new AutoRollback {
      val allResults = Account.findAll()
      allResults.size should be_>(0)
    }
    "count all records" in new AutoRollback {
      val count = Account.countAll()
      count should be_>(0L)
    }
    "find all by where clauses" in new AutoRollback {
      val results = Account.findAllBy(sqls.eq(a.userName, "MyString"))
      results.size should be_>(0)
    }
    "count by where clauses" in new AutoRollback {
      val count = Account.countBy(sqls.eq(a.userName, "MyString"))
      count should be_>(0L)
    }
    "create new record" in new AutoRollback {
      val created = Account.create(userName = "MyString", mailAddress = "MyString", password = "MyString", administrator = false, registeredDate = DateTime.now, updatedDate = DateTime.now, groupAccount = false, fullName = "MyString")
      created should not beNull
    }
    "save a record" in new AutoRollback {
      val entity = Account.findAll().head
      // TODO modify something
      val modified = entity
      val updated = Account.save(modified)
      updated should not equalTo(entity)
    }
    "destroy a record" in new AutoRollback {
      val entity = Account.findAll().head
      Account.destroy(entity)
      val shouldBeNone = Account.find("MyString")
      shouldBeNone.isDefined should beFalse
    }
  }

}
