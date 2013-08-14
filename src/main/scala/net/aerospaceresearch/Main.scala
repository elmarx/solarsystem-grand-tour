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

package net.aerospaceresearch

import net.aerospaceresearch.jplparser.DataReader
import breeze.linalg.DenseVector
import org.joda.time.{DateTimeUtils, DateTime}
import net.aerospaceresearch.model.Body


/**
 * TODO: add documentation
 *
 * User: Elmar Athmer
 * Part of: solarsystem-grand-tour
 */
object Main {

  def main(args: Array[String]) {
    val now: Double = DateTimeUtils.toJulianDay(DateTime.now.toInstant.getMillis)
    val systemNow = new DataReader(now).system
    val systemPlusOne = new DataReader(now + 1).system

    val velocities = systemNow.velocities
    val positions = systemNow.positions

    println("Starting at julian time " + systemNow.time)


    val header = List("Planet",
      "v0X (Ephimeriden)", "v0Y (Ephimeriden)", "v0Z (Ephimeriden)",
      "v1X (Ephimeriden)", "v1Y (Ephimeriden)", "v1Z (Ephimeriden)",
      "v1X (berechnet)", "v1Y (berechnet)", "v1Z (berechnet)",
      "δv1X", "δv1Y", "δv1Z",
      "r0X (Ephimeriden)", "r0Y (Ephimeriden)", "r0Z (Ephimeriden)",
      "r1X (Ephimeriden)", "r1Y (Ephimeriden)", "r1Z (Ephimeriden)",
      "r1X (berechnet)", "r1Y (berechnet)", "r1Z (berechnet)",
      "δr1X", "δr1Y", "δr1Z"
    )

    println(header.mkString(";"))

    val data: List[((Body, (DenseVector[Double], DenseVector[Double])), Body)] =
      systemNow.bodies.zip(velocities.zip(positions)).zip(systemPlusOne.bodies)

    data.map { case ((a0, (v1, r1)), a1) =>
      List(a0.identity.toString,
        a0.v0(0), a0.v0(1), a0.v0(2),
        a1.v0(0), a1.v0(1), a1.v0(2),
        v1(0), v1(1), v1(2),
        v1(0) - a1.v0(0), v1(1) - a1.v0(1), v1(2) - a1.v0(2),
        a0.r0(0), a0.r0(1), a0.r0(2),
        a1.r0(0), a1.r0(1), a1.r0(2),
        r1(0), r1(1), r1(2),
        r1(0) - a1.r0(0), r1(1) - a1.r0(1), r1(2) - a1.r0(2)
      ).mkString(";")
    }.foreach(println)
  }
}
