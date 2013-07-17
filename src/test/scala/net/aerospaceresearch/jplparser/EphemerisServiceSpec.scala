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

package net.aerospaceresearch.jplparser

import org.scalatest.FunSpec
import net.aerospaceresearch.jplparser.data.{ExampleHeaderFile, Ascp1950TestData}
import EntityAssignments.AstronomicalObjects._

class EphemerisServiceSpec extends FunSpec {

  lazy val service =
    new Ascp1950TestData with ExampleHeaderFile {
      val s = JplParser.generateService(headerContent, content)
    }.s

  describe("the EphemerisService") {

    it("calculates the chebyshev time") {
      expectResult(-1.0)(service.chebyshevTime(Mercury, 2433264.5))
      expectResult(0.75)(service.chebyshevTime(Mercury, 2433279.5))
      expectResult(0.875)(service.chebyshevTime(Venus, 2433279.5))
      expectResult(-0.0625)(service.chebyshevTime(Mars, 2433279.5))
      expectResult(0.5)(service.chebyshevTime(Moon_Geocentric, 2433279.5))
    }

    it("calculates the subinterval for a point-in-time") {
      val moon = service.entity(Moon_Geocentric)
      expectResult(3)(service.subInterval(moon, 2433279.5))
    }

    it("returns the position vector") {
      val expectedPosition = (
        0.236503372958839700,
        -0.3069001738761292,
        -0.18869511987388435
        )

      val position = service.position(Mercury, 2433264.5)
      assert(position._1.toDouble === expectedPosition._1)
      assert(position._2.toDouble === expectedPosition._2)
      assert(position._3.toDouble === expectedPosition._3)
    }

    it("calculates the chebyshev position polynoms") {
      val calculator = service.chebychevAt((1, -1), -1) _
      assert(BigDecimal(1.0) === calculator(0))
      assert(BigDecimal(-1.0) === calculator(1))
      assert(BigDecimal(-1.0) === calculator(13))

    }
  }

}
