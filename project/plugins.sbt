logLevel := Level.Warn

libraryDependencies ++= Seq(
  "com.h2database" % "h2" % "1.4.187",
  "mysql" % "mysql-connector-java" % "5.1.36"
)

addSbtPlugin("org.scalikejdbc" %% "scalikejdbc-mapper-generator" % "2.2.7")
