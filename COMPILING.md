Compiling the project from scratch
==================================

To build a distributable package sbt can be utilized. The system where the application should be deployed then only
requires a JVM, no sbt/scala/whatsover.

The [sbt-pack][1] plugin gathers all libraries, puts them into one directory, and provides a shell/batch script to
execute the application.

sbt pack
--------

1. Run `sbt pack`,
2. find the application in *target/pack/bin/ssgt*.

For some command examples see the [sbt-pack Readme][2].


[1]: https://github.com/xerial/sbt-pack "xerial/sbt-pack"
[2]: https://github.com/xerial/sbt-pack#command-examples