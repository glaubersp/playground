import Scalac.Keys._

ThisBuild / scalacOptions ++= Seq(
  "-language:_",
  "-Ymacro-annotations",
  "-Wunused:imports" // always on for OrganizeImports
) ++ Seq("-encoding", "UTF-8") ++ warnings.value ++ lint.value

ThisBuild / insideCI := false
ThisBuild / warnings := {
  if (insideCI.value)
    Seq(
      "-Wconf:any:error", // for scalac warnings
      "-Xfatal-warnings" // for wartremover warts
    )
  else if (lintOn.value)
    Seq("-Wconf:any:warning")
  else
    Seq("-Wconf:any:silent")
}

ThisBuild / lintOn :=
  !sys.env.contains("LINT_OFF")

ThisBuild / lint := {
  if (shouldLint.value)
    Scalac.Lint
  else
    Seq.empty
}

ThisBuild / shouldLint :=
  insideCI.value || lintOn.value

ThisBuild / wartremoverWarnings := {
  if (shouldLint.value)
//    Warts.allBut(
//      Wart.ImplicitConversion,
//      Wart.ImplicitParameter,
//    )
    Warts.allBut(
      Wart.Any,
      Wart.Nothing,
      Wart.Serializable,
      Wart.AnyVal,
      Wart.StringPlusAny,
      Wart.PlatformDefault,
      Wart.Overloading,
      Wart.Recursion
    )
  else
    (ThisBuild / wartremoverWarnings).value
}
