/*
 * Copyright (c) 2013 Elmar Athmer
 *
 * This file is part of SolarSystemGrandTour.
 *
 * SolarSystemGrandTour is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * SolarSystemGrandTour is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with SolarSystemGrandTour.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.aerospaceresearch.csv

import net.aerospaceresearch.model.SolarSystem
import java.io.{PrintWriter, File}
import org.joda.time.{DateTimeUtils, DateTime}


/**
 * TODO: add documentation
 *
 * User: Elmar Athmer
 * Part of: solarsystem-grand-tour
 * Date: 15.08.13
 * Time: 02:06
 */
class SolarSystemsCsvWriter(systems: Seq[SolarSystem]) {

  val file = new File("/home/elmar/gsoc/auto/" + DateTimeUtils.toJulianDay(DateTime.now.toInstant.getMillis) + ".csv")


  val header = "Julian Time" +: systems(0).bodies.map(
    b => List("vX", "vY", "vZ", "rX", "rY", "rZ").map(
      b.identity.toString + " " + _
    ).mkString(";")
  )


  val content = systems.par.map(
    system => system.time + ";" + system.bodies.map(b => (b.v0.toArray ++ b.r0.toArray).mkString(";")).mkString(";")
  )

  val out = new PrintWriter(file)

  out.println(header.mkString(";"))
  content.map(out.println)

  out.close()
}
