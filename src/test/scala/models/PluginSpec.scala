package models

import models.gitbucket.Plugin
import scalikejdbc.specs2.mutable.AutoRollback
import org.specs2.mutable._
import scalikejdbc._


class PluginSpec extends Specification {

  "Plugin" should {

    val p = Plugin.syntax("p")

    "find by primary keys" in new AutoRollback {
      val maybeFound = Plugin.find("MyString")
      maybeFound.isDefined should beTrue
    }
    "find by where clauses" in new AutoRollback {
      val maybeFound = Plugin.findBy(sqls.eq(p.pluginId, "MyString"))
      maybeFound.isDefined should beTrue
    }
    "find all records" in new AutoRollback {
      val allResults = Plugin.findAll()
      allResults.size should be_>(0)
    }
    "count all records" in new AutoRollback {
      val count = Plugin.countAll()
      count should be_>(0L)
    }
    "find all by where clauses" in new AutoRollback {
      val results = Plugin.findAllBy(sqls.eq(p.pluginId, "MyString"))
      results.size should be_>(0)
    }
    "count by where clauses" in new AutoRollback {
      val count = Plugin.countBy(sqls.eq(p.pluginId, "MyString"))
      count should be_>(0L)
    }
    "create new record" in new AutoRollback {
      val created = Plugin.create(pluginId = "MyString", version = "MyString")
      created should not beNull
    }
    "save a record" in new AutoRollback {
      val entity = Plugin.findAll().head
      // TODO modify something
      val modified = entity
      val updated = Plugin.save(modified)
      updated should not equalTo(entity)
    }
    "destroy a record" in new AutoRollback {
      val entity = Plugin.findAll().head
      Plugin.destroy(entity)
      val shouldBeNone = Plugin.find("MyString")
      shouldBeNone.isDefined should beFalse
    }
  }

}
