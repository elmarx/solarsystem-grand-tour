import AssemblyKeys._

name := "SolarSystemGrandTour"

version := "0.5.0"

scalaVersion := "2.10.4"

scalacOptions ++= Seq("-deprecation", "-feature")

libraryDependencies ++= Seq(
  "org.scalanlp" % "breeze_2.10" % "0.7",
  "org.scalanlp" % "breeze-natives_2.10" % "0.7",
  "org.scalatest" % "scalatest_2.10" % "2.1.3" % "test",
  "com.github.nscala-time" %% "nscala-time" % "0.8.0",
  "org.apache.commons" % "commons-math3" % "3.2"
)

assemblySettings
