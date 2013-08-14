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
import org.joda.time.DateTime
import scala.collection.parallel.immutable.ParSeq
import net.aerospaceresearch.model.Body


/**
 * TODO: add documentation
 *
 * User: Elmar Athmer
 * Part of: solarsystem-grand-tour
 */
object Main {

  def main(args: Array[String]) {
    val startedAt = DateTime.now.toInstant.getMillis

    val system = DataReader.current().system
    val velocities = system.velocities
    val positions = system.positions


    val header = List("Planet", "vX", "vY", "vZ", "rX", "rY", "rZ")

    println(header.mkString(";"))

    val data: List[(Body, (DenseVector[Double], DenseVector[Double]))] = system.bodies.zip(velocities.zip(positions))
    data.map { case (a, (v, r)) =>
      List(a.identity.toString, v(0), v(1), v(2), r(0), r(1), r(2)).mkString(";")
    }.foreach(println)


    val time = DateTime.now.toInstant.getMillis - startedAt

    println("This task ran " + time + " milliseconds")
  }
}
