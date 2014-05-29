packSettings

packMain := Map("ssgt" -> "net.aerospaceresearch.Main")

name := "SolarSystemGrandTour"

version := "0.6.0"

scalaVersion := "2.11.1"

scalacOptions ++= Seq("-deprecation", "-feature")

libraryDependencies ++= Seq(
  "org.scalanlp" %% "breeze" % "0.8.1",
  "org.scalanlp" %% "breeze-natives" % "0.8.1",
  "org.scalatest" %% "scalatest" % "2.1.7" % "test",
  "com.github.nscala-time" %% "nscala-time" % "1.2.0",
  "org.apache.commons" % "commons-math3" % "3.3",
  "org.scala-lang.modules" %% "scala-xml" % "1.0.2"
)


resolvers ++= Seq(
    // other resolvers here
    // if you want to use snapshot builds (currently 0.8-SNAPSHOT), use this.
    "Sonatype Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/",
    "Sonatype Releases" at "https://oss.sonatype.org/content/repositories/releases/"
)
