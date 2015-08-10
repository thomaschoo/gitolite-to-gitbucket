package models.gitbucket

import org.joda.time.DateTime
import org.specs2.mutable._
import scalikejdbc._
import scalikejdbc.specs2.mutable.AutoRollback

class ActivitySpec extends Specification {

  "Activity" should {

    val a = Activity.syntax("a")

    "find by primary keys" in new AutoRollback {
      val maybeFound = Activity.find(123)
      maybeFound.isDefined should beTrue
    }
    "find by where clauses" in new AutoRollback {
      val maybeFound = Activity.findBy(sqls.eq(a.activityId, 123))
      maybeFound.isDefined should beTrue
    }
    "find all records" in new AutoRollback {
      val allResults = Activity.findAll()
      allResults.size should be_>(0)
    }
    "count all records" in new AutoRollback {
      val count = Activity.countAll()
      count should be_>(0L)
    }
    "find all by where clauses" in new AutoRollback {
      val results = Activity.findAllBy(sqls.eq(a.activityId, 123))
      results.size should be_>(0)
    }
    "count by where clauses" in new AutoRollback {
      val count = Activity.countBy(sqls.eq(a.activityId, 123))
      count should be_>(0L)
    }
    "create new record" in new AutoRollback {
      val created = Activity.create(userName = "MyString", repositoryName = "MyString", activityUserName = "MyString", activityType = "MyString", message = null, activityDate = DateTime.now)
      created should not beNull
    }
    "save a record" in new AutoRollback {
      val entity = Activity.findAll().head
      // TODO modify something
      val modified = entity
      val updated = Activity.save(modified)
      updated should not equalTo(entity)
    }
    "destroy a record" in new AutoRollback {
      val entity = Activity.findAll().head
      Activity.destroy(entity)
      val shouldBeNone = Activity.find(123)
      shouldBeNone.isDefined should beFalse
    }
  }

}
