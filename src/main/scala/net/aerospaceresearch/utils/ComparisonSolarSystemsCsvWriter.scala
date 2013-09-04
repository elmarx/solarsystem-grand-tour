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

import net.aerospaceresearch.model.{Body, SolarSystem}
import java.io.{PrintWriter, File}
import org.joda.time.{DateTimeUtils, DateTime}
import java.text.{DecimalFormatSymbols, DecimalFormat}
import java.util.Locale


/**
 * TODO: add documentation
 *
 * User: Elmar Athmer
 * Part of: solarsystem-grand-tour
 * Date: 15.08.13
 * Time: 02:06
 */
class ComparisonSolarSystemsCsvWriter(comparison: Seq[(SolarSystem, SolarSystem)], outputDir: String) {

  val file = {
    val now: DateTime = DateTime.now
    val filename = "%s_j%s".format(
      now.toString("yyyy-MM-dd_HH-mm-ss"),
      new DecimalFormat("#.###", new DecimalFormatSymbols(Locale.ENGLISH)).
        format(DateTimeUtils.toJulianDay(now.toInstant.getMillis))
    )
    new File(outputDir + "/" + filename + ".csv")
  }

  
  val header = "Julian Time" +: comparison(0)._1.bodies.map(b =>
    List("vX(JPL)", "vY(JPL)", "vZ(JPL)",
      "vX(my)", "vY(my)", "vZ(my)",
      "|v(JPL)| - |v(my)|",

      "rX(JPL)", "rY(JPL)", "rZ(JPL)",
      "rX(my)", "rY(my)", "rZ(my)",
      "|r(JPL)| - |r(my)|"
    ).map(b.name + " " + _).mkString(";")
  )

  val content: Seq[String] = comparison.map { case(jpl, my) =>
    // a row is a list of bodies, but each bodies twice, from jpl and from "my" calculation
    val row: Seq[(Body, Body)] = jpl.bodies.toList.zip(my.bodies.toList)

    val columns: Seq[Double] = jpl.time.value +: row.map{ case(jplBody, myBody) =>
      jplBody.v0.toArray ++ myBody.v0.toArray ++
      Array(jplBody.v0.norm(2) - myBody.v0.norm(2)) ++
      jplBody.r0.toArray ++ myBody.r0.toArray ++
      Array(jplBody.r0.norm(2) - myBody.r0.norm(2))
    }.flatten

    columns.mkString(";")
  }


  val out = new PrintWriter(file)

  out.println(header.mkString(";"))
  content.map(out.println)

  out.close()
}
