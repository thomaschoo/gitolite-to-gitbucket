package com.thomaschoo.services

import com.thomaschoo.helpers.Config
import com.zaxxer.hikari.HikariDataSource

import scalikejdbc.{ConnectionPool, DataSourceConnectionPool}

object DB {
  def setup() = {
    GitoliteDB.setup()
    GitBucketDB.setup()
  }
}

private object GitoliteDB {
  Class.forName("com.mysql.jdbc.Driver")

  def setup() {
    val ds = new HikariDataSource()
    ds.setDataSourceClassName("com.mysql.jdbc.jdbc2.optional.MysqlDataSource")
    ds.addDataSourceProperty("url", Config.gitoliteDbUrl)
    ds.addDataSourceProperty("user", Config.gitoliteDbUser)
    ds.addDataSourceProperty("password", Config.gitoliteDbPassword)

    ConnectionPool.add('gitolite, new DataSourceConnectionPool(ds))
  }
}

private object GitBucketDB {
  Class.forName("org.h2.Driver")

  def setup() {
    val ds = new HikariDataSource()
    ds.setDataSourceClassName("org.h2.jdbcx.JdbcDataSource")
    ds.addDataSourceProperty("url", Config.gitBucketDbUrl)
    ds.addDataSourceProperty("user", Config.gitBucketDbUser)
    ds.addDataSourceProperty("password", Config.gitBucketDbPassword)

    ConnectionPool.add('gitBucket, new DataSourceConnectionPool(ds))
  }
}
