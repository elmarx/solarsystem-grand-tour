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

import model._
import breeze.linalg._


/**
 * TODO: add documentation
 *
 * User: Elmar Athmer
 * Part of: solarsystem-grand-tour
 */
object Main {

  def main(args: Array[String]) {
    val centerMass = new Body(10000, DenseVector(0, 0, 0), DenseVector(0, 0, 0))

    val bodies = List(
      new Body(1, DenseVector(1, 1 ,1), DenseVector(2, 2 , 2)),
      new Body(1, DenseVector(2, 1, 1), DenseVector(2, 2, 2))
    )

    val system = new SolarSystem(Nil, centerMass, 0)
  }
}
