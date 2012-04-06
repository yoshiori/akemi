import sbt._
import sbt.Keys._

object ProjectBuild extends Build {

  lazy val root = Project(
    id = "root",
    base = file("."),
    settings = Project.defaultSettings ++ Seq(
      name := "akemi",
      organization := "org.yoshiori",
      version := "0.1-SNAPSHOT",
      scalaVersion := "2.9.1",
      resolvers += "twitter-repo" at "http://maven.twttr.com",

      libraryDependencies ++= Seq(
        "com.twitter" %% "finagle-kestrel" % "3.0.0",
        "com.twitter" %% "finagle-redis" % "3.0.0"
      )
    )
  )
}
