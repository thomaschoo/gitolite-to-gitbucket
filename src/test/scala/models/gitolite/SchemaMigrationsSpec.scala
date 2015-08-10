package models.gitolite

import org.specs2.mutable._


class SchemaMigrationsSpec extends Specification {

  "SchemaMigrations" should {

    val sm = SchemaMigrations.syntax("sm")

    "find by primary keys" in new AutoRollback {
      val maybeFound = SchemaMigrations.find()
      maybeFound.isDefined should beTrue
    }
    "find by where clauses" in new AutoRollback {
      val maybeFound = SchemaMigrations.findBy()
      maybeFound.isDefined should beTrue
    }
    "find all records" in new AutoRollback {
      val allResults = SchemaMigrations.findAll()
      allResults.size should be_>(0)
    }
    "count all records" in new AutoRollback {
      val count = SchemaMigrations.countAll()
      count should be_>(0L)
    }
    "find all by where clauses" in new AutoRollback {
      val results = SchemaMigrations.findAllBy()
      results.size should be_>(0)
    }
    "count by where clauses" in new AutoRollback {
      val count = SchemaMigrations.countBy()
      count should be_>(0L)
    }
    "create new record" in new AutoRollback {
      val created = SchemaMigrations.create(version = "MyString")
      created should not beNull
    }
    "save a record" in new AutoRollback {
      val entity = SchemaMigrations.findAll().head
      // TODO modify something
      val modified = entity
      val updated = SchemaMigrations.save(modified)
      updated should not equalTo(entity)
    }
    "destroy a record" in new AutoRollback {
      val entity = SchemaMigrations.findAll().head
      SchemaMigrations.destroy(entity)
      val shouldBeNone = SchemaMigrations.find()
      shouldBeNone.isDefined should beFalse
    }
  }

}
