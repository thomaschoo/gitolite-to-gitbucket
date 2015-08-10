package models.gitolite

import org.joda.time.DateTime
import org.specs2.mutable._

import scalikejdbc._
import scalikejdbc.specs2.mutable.AutoRollback

class UsersProjectsSpec extends Specification {

  "UsersProjects" should {

    val up = UsersProjects.syntax("up")

    "find by primary keys" in new AutoRollback {
      val maybeFound = UsersProjects.find(123)
      maybeFound.isDefined should beTrue
    }
    "find by where clauses" in new AutoRollback {
      val maybeFound = UsersProjects.findBy(sqls.eq(up.id, 123))
      maybeFound.isDefined should beTrue
    }
    "find all records" in new AutoRollback {
      val allResults = UsersProjects.findAll()
      allResults.size should be_>(0)
    }
    "count all records" in new AutoRollback {
      val count = UsersProjects.countAll()
      count should be_>(0L)
    }
    "find all by where clauses" in new AutoRollback {
      val results = UsersProjects.findAllBy(sqls.eq(up.id, 123))
      results.size should be_>(0)
    }
    "count by where clauses" in new AutoRollback {
      val count = UsersProjects.countBy(sqls.eq(up.id, 123))
      count should be_>(0L)
    }
    "create new record" in new AutoRollback {
      val created = UsersProjects.create(userId = 123, projectId = 123, createdAt = DateTime.now, updatedAt = DateTime.now, projectAccess = 123)
      created should not beNull
    }
    "save a record" in new AutoRollback {
      val entity = UsersProjects.findAll().head
      // TODO modify something
      val modified = entity
      val updated = UsersProjects.save(modified)
      updated should not equalTo(entity)
    }
    "destroy a record" in new AutoRollback {
      val entity = UsersProjects.findAll().head
      UsersProjects.destroy(entity)
      val shouldBeNone = UsersProjects.find(123)
      shouldBeNone.isDefined should beFalse
    }
  }

}
