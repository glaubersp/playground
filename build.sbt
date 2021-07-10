import Dependencies._

ThisBuild / organization := "dev.insideyou"
ThisBuild / scalaVersion := "2.13.6"

lazy val `playground` =
  project
    .in(file("."))
    .settings(name := "playground")
    .settings(commonSettings)
    .settings(dependencies)

lazy val commonSettings = Seq(
  addCompilerPlugin(com.olegpy.betterMonadicFor),
  addCompilerPlugin(org.augustjune.contextApplied),
  addCompilerPlugin(org.typelevel.kindProjector),
  update / evictionWarningOptions := EvictionWarningOptions.empty,
  Compile / console / scalacOptions := {
    (Compile / console / scalacOptions)
      .value
      .filterNot(_.contains("wartremover"))
      .filterNot(Scalac.Lint.toSet)
      .filterNot(Scalac.FatalWarnings.toSet) :+ "-Wconf:any:silent"
  },
  Test / console / scalacOptions :=
    (Compile / console / scalacOptions).value
)

lazy val dependencies = Seq(
  libraryDependencies ++= Seq(
    // main dependencies
    dev.zio.zio,
    dev.zio.zioTest,
    dev.zio.zioMacros
  ),
  libraryDependencies ++= Seq(
    com.github.alexarchambault.scalacheckShapeless_1_15,
    org.scalacheck.scalacheck,
    org.scalatest.scalatest,
    org.scalatestplus.scalacheck_1_15,
    org.typelevel.disciplineScalatest
  ).map(_ % Test)
)
