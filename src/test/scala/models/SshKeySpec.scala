package models

import models.gitbucket.SshKey
import scalikejdbc.specs2.mutable.AutoRollback
import org.specs2.mutable._
import scalikejdbc._


class SshKeySpec extends Specification {

  "SshKey" should {

    val sk = SshKey.syntax("sk")

    "find by primary keys" in new AutoRollback {
      val maybeFound = SshKey.find(123, "MyString")
      maybeFound.isDefined should beTrue
    }
    "find by where clauses" in new AutoRollback {
      val maybeFound = SshKey.findBy(sqls.eq(sk.sshKeyId, 123))
      maybeFound.isDefined should beTrue
    }
    "find all records" in new AutoRollback {
      val allResults = SshKey.findAll()
      allResults.size should be_>(0)
    }
    "count all records" in new AutoRollback {
      val count = SshKey.countAll()
      count should be_>(0L)
    }
    "find all by where clauses" in new AutoRollback {
      val results = SshKey.findAllBy(sqls.eq(sk.sshKeyId, 123))
      results.size should be_>(0)
    }
    "count by where clauses" in new AutoRollback {
      val count = SshKey.countBy(sqls.eq(sk.sshKeyId, 123))
      count should be_>(0L)
    }
    "create new record" in new AutoRollback {
      val created = SshKey.create(userName = "MyString", title = "MyString", publicKey = null)
      created should not beNull
    }
    "save a record" in new AutoRollback {
      val entity = SshKey.findAll().head
      // TODO modify something
      val modified = entity
      val updated = SshKey.save(modified)
      updated should not equalTo(entity)
    }
    "destroy a record" in new AutoRollback {
      val entity = SshKey.findAll().head
      SshKey.destroy(entity)
      val shouldBeNone = SshKey.find(123, "MyString")
      shouldBeNone.isDefined should beFalse
    }
  }

}
