package models.gitolite

import org.joda.time.DateTime
import org.specs2.mutable._

import scalikejdbc._
import scalikejdbc.specs2.mutable.AutoRollback

class UsersSpec extends Specification {

  "Users" should {

    val u = Users.syntax("u")

    "find by primary keys" in new AutoRollback {
      val maybeFound = Users.find(123)
      maybeFound.isDefined should beTrue
    }
    "find by where clauses" in new AutoRollback {
      val maybeFound = Users.findBy(sqls.eq(u.id, 123))
      maybeFound.isDefined should beTrue
    }
    "find all records" in new AutoRollback {
      val allResults = Users.findAll()
      allResults.size should be_>(0)
    }
    "count all records" in new AutoRollback {
      val count = Users.countAll()
      count should be_>(0L)
    }
    "find all by where clauses" in new AutoRollback {
      val results = Users.findAllBy(sqls.eq(u.id, 123))
      results.size should be_>(0)
    }
    "count by where clauses" in new AutoRollback {
      val count = Users.countBy(sqls.eq(u.id, 123))
      count should be_>(0L)
    }
    "create new record" in new AutoRollback {
      val created = Users.create(email = "MyString", encryptedPassword = "MyString", createdAt = DateTime.now, updatedAt = DateTime.now, admin = false, skype = "MyString", linkedin = "MyString", twitter = "MyString", darkScheme = false, themeId = 123, blocked = false)
      created should not beNull
    }
    "save a record" in new AutoRollback {
      val entity = Users.findAll().head
      // TODO modify something
      val modified = entity
      val updated = Users.save(modified)
      updated should not equalTo(entity)
    }
    "destroy a record" in new AutoRollback {
      val entity = Users.findAll().head
      Users.destroy(entity)
      val shouldBeNone = Users.find(123)
      shouldBeNone.isDefined should beFalse
    }
  }

}
