val scala3Version = "3.4.1"

lazy val root = project
  .in(file("."))
  .settings(
    name := "tinkoff-homework4",
    version := "0.1.0-SNAPSHOT",

    scalaVersion := scala3Version,

    libraryDependencies ++= Seq(
      "org.scalameta" %% "munit" % "0.7.29" % Test,
      "org.scalatest" %% "scalatest" % "3.2.17" % Test,
      "io.circe" %% "circe-core" % "0.15.0-M1",
      "io.circe" %% "circe-parser" % "0.15.0-M1"
    ),
    testFrameworks += new TestFramework("munit.Framework"),
    testFrameworks += new TestFramework("org.scalatest.tools.Framework")
  )
