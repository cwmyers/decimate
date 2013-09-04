import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "decimate"
  val appVersion      = "0.1-SNAPSHOT"
  val appScalaVersion    = "2.10.2"

  val sharedDependencies = Seq(
    "org.scalaz" %% "scalaz-core" % "7.0.2",
    "org.scalaz" %% "scalaz-effect" % "7.0.2",
    "io.argonaut" %% "argonaut" % "6.0.1"
  )

  val appDependencies = Seq(
  )

  val transcoderService = Project("transcoder-service", file("modules/encoder"))
          .settings(scalaVersion := appScalaVersion)
          .settings(libraryDependencies := sharedDependencies ++ Seq(
    "play" %% "play" % "2.1.3",
    "play" %% "play-jdbc" % "2.1.3"
  ))
          .settings(net.virtualvoid.sbt.graph.Plugin.graphSettings: _*)
          .settings(
    resolvers ++= Seq(
      "typesafe" at "http://repo.typesafe.com/typesafe/repo"
    )
  )
          .settings(publishArtifact in (Test, packageBin) := true)

  val main = play.Project(appName, appVersion, sharedDependencies ++ appDependencies)
          .settings(defaultScalaSettings: _*)
          .settings(scalaVersion := appScalaVersion)
          .settings(
    javaOptions in Test += "-Dconfig.file=" + "conf/"+ Option(System.getenv("TEST_CONFIG_FILE")).getOrElse("test.conf")
  )
          .settings(
    resolvers ++= Seq(
      "test argonaut" at "https://oss.sonatype.org/content/repositories/snapshots"
    )).dependsOn(transcoderService % "test->test;compile->compile")
          .settings(net.virtualvoid.sbt.graph.Plugin.graphSettings: _*)
          .settings(parallelExecution in Test := false)
          .settings(testOptions in Test += Tests.Setup( () => println("Setup") ))
          .settings(testOptions in Test += Tests.Cleanup( () => println("Cleanup") )).aggregate(transcoderService)

}