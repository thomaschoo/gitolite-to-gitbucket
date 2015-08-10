lazy val commonSettings = Seq(
  organization := "com.thomaschoo",
  version := "0.1",
  scalaVersion := "2.11.6",
  libraryDependencies ++= Seq(
    "com.typesafe" % "config" % "1.2.1",
    "org.specs2" %% "specs2-core" % "3.5" % "test",
    "com.h2database" % "h2" % "1.4.187",
    "mysql" % "mysql-connector-java" % "5.1.36",
    "org.scalikejdbc" %% "scalikejdbc" % "2.2.7",
    "org.scalikejdbc" %% "scalikejdbc-config" % "2.2.7",
    "ch.qos.logback"  %  "logback-classic"   % "1.1.3"
  ),
  resolvers ++= Seq(
    Resolver.typesafeRepo("releases"),
    Resolver.sonatypeRepo("releases"),
    "Scalaz Bintray Repo" at "http://dl.bintray.com/scalaz/releases"
  ),
  scalacOptions in Test ++= Seq("-Yrangepos")
)

lazy val core = (project in file(".")).
  settings(commonSettings: _*).
  settings(
    name := "gitolite-to-gitbucket",
    libraryDependencies ++= Seq(
    )
  )

scalikejdbcSettings
