name := "SolarSystemGrandTour"

version := "1.0"

scalaVersion := "2.10.2"

libraryDependencies  ++= Seq(
    "org.scalanlp" %% "breeze-math" % "0.4-SNAPSHOT",
    "org.scalatest" % "scalatest_2.10" % "1.9.1" % "test"
)

resolvers ++= Seq(
    "Sonatype Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/"
)