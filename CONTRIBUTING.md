Contributing/Developing
=======================

This little guide should help newcomers to kickstart development.


Buildtool
---------

This project utilizes [sbt][1] as build tool. SBT pulls in all requirements, even Scala itself,
it only requires the Java SDK.

You can install sbt for your system according to the official [setup guide][2].

Developing
----------

If you want to change code, you need of course be familiar with *scala*. If you're familiar with scala,
I assume you're familiar with sbt.

If you want to use this project for "learning-by-doing" scala, the following commands might help you to explore this
project a bit:

1. start sbt command line tool simply by running `sbt`, now you've got the sbt prompt
2. enter `run` to run the project's main class.
3. enter `test` to execute the unit tests
4. prefix any command with *~* to make sbt watch for changed files. If any file changes, the command is being re-run.
5. so, use `~test` for test-driven development

[1]: http://www.scala-sbt.org/ "Simple Build Tool"
[2]: http://www.scala-sbt.org/release/docs/Getting-Started/Setup.html "SBT Setup Instructions"

Tasks/TODOs
-----------

See [github issues][1].


[1]: https://github.com/zauberpony/solarsystem-grand-tour/issues