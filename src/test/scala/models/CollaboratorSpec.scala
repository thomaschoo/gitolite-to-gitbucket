package models

import models.gitbucket.Collaborator
import scalikejdbc.specs2.mutable.AutoRollback
import org.specs2.mutable._
import scalikejdbc._


class CollaboratorSpec extends Specification {

  "Collaborator" should {

    val c = Collaborator.syntax("c")

    "find by primary keys" in new AutoRollback {
      val maybeFound = Collaborator.find("MyString", "MyString", "MyString")
      maybeFound.isDefined should beTrue
    }
    "find by where clauses" in new AutoRollback {
      val maybeFound = Collaborator.findBy(sqls.eq(c.collaboratorName, "MyString"))
      maybeFound.isDefined should beTrue
    }
    "find all records" in new AutoRollback {
      val allResults = Collaborator.findAll()
      allResults.size should be_>(0)
    }
    "count all records" in new AutoRollback {
      val count = Collaborator.countAll()
      count should be_>(0L)
    }
    "find all by where clauses" in new AutoRollback {
      val results = Collaborator.findAllBy(sqls.eq(c.collaboratorName, "MyString"))
      results.size should be_>(0)
    }
    "count by where clauses" in new AutoRollback {
      val count = Collaborator.countBy(sqls.eq(c.collaboratorName, "MyString"))
      count should be_>(0L)
    }
    "create new record" in new AutoRollback {
      val created = Collaborator.create(userName = "MyString", repositoryName = "MyString", collaboratorName = "MyString")
      created should not beNull
    }
    "save a record" in new AutoRollback {
      val entity = Collaborator.findAll().head
      // TODO modify something
      val modified = entity
      val updated = Collaborator.save(modified)
      updated should not equalTo(entity)
    }
    "destroy a record" in new AutoRollback {
      val entity = Collaborator.findAll().head
      Collaborator.destroy(entity)
      val shouldBeNone = Collaborator.find("MyString", "MyString", "MyString")
      shouldBeNone.isDefined should beFalse
    }
  }

}
