import sbt._
import sbt.Keys._

object ProjectBuild extends Build {

  val twitterLibVersion = "3.0.0"

  lazy val root = Project(
    id = "root",
    base = file("."),
    settings = Project.defaultSettings ++ Seq(
      name := "akemi",
      organization := "org.yoshiori.akemi",
      version := "0.1-SNAPSHOT",
      scalaVersion := "2.9.1",
      resolvers += "twitter-repo" at "http://maven.twttr.com",

      libraryDependencies ++= Seq(
        // test
        "org.specs2" %% "specs2" % "1.9" % "test",
            
        // twitter lib
        "com.twitter" %% "finagle-kestrel" % twitterLibVersion,
        "com.twitter" %% "finagle-redis" % twitterLibVersion,
        "com.twitter" %% "util-eval" % twitterLibVersion,

        //log
        "org.clapper" %% "grizzled-slf4j" % "0.6.8",
        "ch.qos.logback" % "logback-classic" % "1.0.1"
      )
    )
  )
}
