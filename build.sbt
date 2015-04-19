name := "gitolite-to-gitbucket"

organization := "com.thomaschoo"

version := "0.1"

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  "com.typesafe" % "config" % "1.2.1",
  "org.specs2" %% "specs2-core" % "3.5" % "test"
)

resolvers ++= Seq(
)

scalacOptions in Test ++= Seq("-Yrangepos")
