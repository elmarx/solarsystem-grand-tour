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

package net.aerospaceresearch.utils

import net.aerospaceresearch.model.SolarSystem
import java.io.{PrintWriter, File}
import org.joda.time.{DateTimeUtils, DateTime}
import org.joda.time.format.{DateTimeFormatter, DateTimeFormat}
import java.text.{DecimalFormatSymbols, DecimalFormat, NumberFormat}
import java.util.Locale


/**
 * TODO: add documentation
 *
 * User: Elmar Athmer
 * Part of: solarsystem-grand-tour
 * Date: 15.08.13
 * Time: 02:06
 */
class SolarSystemsCsvWriter(systems: Seq[SolarSystem], outputDir: String) {

  val file = {
    val now: DateTime = DateTime.now
    val filename = "%s_j%s".format(
      now.toString("yyyy-MM-dd_HH-mm-ss"),
      new DecimalFormat("#.###", new DecimalFormatSymbols(Locale.ENGLISH)).
        format(DateTimeUtils.toJulianDay(now.toInstant.getMillis))
    )
    new File(outputDir + "/" + filename + ".csv")
  }


  val header = "Julian Time" +: systems(0).bodies.map(
    b => List("vX", "vY", "vZ", "rX", "rY", "rZ").map(
      b.name + " " + _
    ).mkString(";")
  )


  val content = systems.map {
    system => system.time.value + ";" + system.bodies.map(
      b => (b.v0.toArray ++ b.r0.toArray).mkString(";")
    ).mkString(";")
  }

  val out = new PrintWriter(file)

  out.println(header.mkString(";"))
  content.map(out.println)

  out.close()
}
