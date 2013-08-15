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
import net.aerospaceresearch.model.{SolarSystem, Body}
import net.aerospaceresearch.csv.SolarSystemsCsvWriter


/**
 * TODO: add documentation
 *
 * User: Elmar Athmer
 * Part of: solarsystem-grand-tour
 */
object Main {

  def main(args: Array[String]) {
    val start: Double = 2456520

    0.to(365, 10).map { x =>
      println(x)

      val systemNow = new DataReader(start + x).system
      val next: List[SolarSystem] = systemNow.goto(start + x + 10)

      new SolarSystemsCsvWriter(next.reverse)
    }




  }
}
