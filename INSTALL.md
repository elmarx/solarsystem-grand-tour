Installation/Compilation
========================

Building
--------

This project utilizes [sbt][1] as build tool. If you're familiar with sbt,
there should be nothing special about this project.

1. install sbt for your system according to the official [setup guide][2]
2. run `sbt assembly` inside the project root, to generate a jar file. You'll find it in
*target/scala-2.xx/SolarSystemGrandTour-assembly-0.x.0\*.jar*

This file contains everything needed to execute the ssgt.

Running
-------

Execute the jar file like any java application with `java -jar SolarSystemGrandTour*.jar`.

Although building is straight forward, running is a bit quirky, due to the current development state of this project
(focus is on functionality, not usability).

The ssgt expects the following to be present:

* an input.xml file in the current working directory, containing settings. The best would be for you to copy *input
.xml* and change it according to your needs
* ephemerides-data [provided by NASA][3], please mirror the complete *de423* folder to the current working directory

These quirkiness is subject to change, but with low priority.

Developing
----------

If you want to change code, you need of course be familiar with *scala*. If you're familiar with scala,
I assume you're familiar with sbt.

If you want to use this project for "learning-by-doing" scala, the following commands might help you to explore this
project a bit:

1. start sbt command line tool simply by running `sbt`, you know have the sbt prompt
2. enter `run` to run the project's main class.
3. enter `test` to execute the unit tests
4. prefix any command with *~* to make sbt watch for changed files. If any file changes, the command is being re-run.
5. so, use `~test` for test-driven development


[1]: http://www.scala-sbt.org/ "Simple Build Tool"
[2]: http://www.scala-sbt.org/release/docs/Getting-Started/Setup.html "SBT Setup Instructions"
[3]: ftp://ssd.jpl.nasa.gov/pub/eph/planets/ascii/de423/ "Ehpimerides data provided by NASA's HORIZON project"
