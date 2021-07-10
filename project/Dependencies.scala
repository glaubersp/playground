import sbt._

object Dependencies {

  case object co {
    val fs2Core = "co.fs2" %% "fs2-core" % "2.5.9"
  }

  case object com {
    case object github {
      case object alexarchambault {
        val scalacheckShapeless_1_15 =
          "com.github.alexarchambault" %% "scalacheck-shapeless_1.15" % "1.3.0"
        val pureconfig = "com.github.pureconfig" %% "pureconfig" % "0.16.0"
      }

      case object h2database {
        val h2 = "com.h2database" % "h2" % "1.4.200"
      }

      case object liancheng {
        val organizeImports =
          "com.github.liancheng" %% "organize-imports" % "0.5.0"
      }
    }

    case object olegpy {
      val betterMonadicFor =
        "com.olegpy" %% "better-monadic-for" % "0.3.1"
    }
  }

  case object dev {
    case object zio {
      val zio = "dev.zio" %% "zio" % "1.0.9"
      val zioInteropCats =
        ("dev.zio" %% "zio-interop-cats" % "2.5.1.0").excludeAll(ExclusionRule("dev.zio"))
      val zioMacros = "dev.zio" %% "zio-macros" % "1.0.9"
      val zioTest = "dev.zio" %% "zio-test" % "1.0.9" % "test"
      val zioTestSbt = "dev.zio" %% "zio-test-sbt" % "1.0.9" % "test"
    }
  }

  case object io {
    case object circle {
      val circeGeneric = "io.circe" %% "circe-generic" % "0.14.1"
      val circeCore = "io.circe" %% "circe-core" % "0.14.1"
      val circeParser = "io.circe" %% "circe-parser" % "0.14.1"
      val circeExtras = "io.circe" %% "circe-generic-extras" % "0.14.1"
      val circe = List(circeGeneric, circeCore, circeParser, circeExtras)
    }
  }

  case object org {
    case object slf4j {
      val slf4j_simple = "org.slf4j" % "slf4j-simple" % "1.7.31"
    }

    case object augustjune {
      val canoe = "org.augustjune" %% "canoe" % "0.5.1"
      val contextApplied = "org.augustjune" %% "context-applied" % "0.1.4"
    }

    case object flywaydb {
      val flyway = "org.flywaydb" % "flyway-core" % "7.11.1"
    }

    case object scalacheck {
      val scalacheck =
        "org.scalacheck" %% "scalacheck" % "1.15.4"
    }

    case object scalatest {
      val scalatest =
        "org.scalatest" %% "scalatest" % "3.2.9"
    }

    case object scalatestplus {
      val scalacheck_1_15 =
        "org.scalatestplus" %% "scalacheck-1-15" % "3.2.9.0"
    }

    case object typelevel {
      val disciplineScalatest =
        "org.typelevel" %% "discipline-scalatest" % "2.1.5"

      val kindProjector =
        "org.typelevel" %% "kind-projector" % "0.13.0" cross CrossVersion.full
    }

    case object tpolecat {
      val doobieCore = "org.tpolecat" %% "doobie-core" % "0.13.4"
      val doobieH2 = "org.tpolecat" %% "doobie-h2" % "0.13.4"
      val doobieHikari = "org.tpolecat" %% "doobie-hikari" % "0.13.4"
      val doobie = List(doobieCore, doobieH2, doobieHikari)
    }

  }
}
