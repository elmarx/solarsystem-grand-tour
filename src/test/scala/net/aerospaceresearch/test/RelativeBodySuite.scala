/*
 * Copyright (c) 2014 Elmar Athmer
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

package net.aerospaceresearch.test

import org.scalatest.FunSuite
import net.aerospaceresearch.model.{Planet, RelativeBody, Probe}
import breeze.linalg.DenseVector
import org.scalatest.Matchers._

class RelativeBodySuite extends FunSuite {

  // just testing the happy path here (i.e.: assumed usage)
  test("can resolve relative positions for probes") {
    val myProbe = Probe("myProbe", 42, DenseVector[Double](4, 8, 15), DenseVector[Double](16, 23, 41))
    val relativeProbe = RelativeBody(myProbe, "some_system")

    val bodies = List(
      Planet("a", 100, DenseVector[Double](-10, 20, 30), DenseVector[Double](-20, -23, -24)),
      Planet("b", 100, DenseVector[Double](-10, 20, 30), DenseVector[Double](-20, -23, -24)),
      Planet("some_system", 100, DenseVector[Double](-100, 211, 333), DenseVector[Double](17, 42, 23)),
      Planet("c", 100, DenseVector[Double](-10, 20, 30), DenseVector[Double](-20, -23, -24))
    )

    relativeProbe.resolve(bodies) === Probe("myProbe", 42,
      DenseVector[Double](-96, 219, 348),
      DenseVector[Double](33, 0, 17)
    )

  }

}
