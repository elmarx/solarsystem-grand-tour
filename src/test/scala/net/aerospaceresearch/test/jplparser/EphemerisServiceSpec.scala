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

package net.aerospaceresearch.test.jplparser

import org.scalatest.FunSpec
import net.aerospaceresearch.jplparser.{JplParser, EntityAssignments}
import EntityAssignments.AstronomicalObjects._
import net.aerospaceresearch.utils.SiConverter._
import org.scalatest.matchers.ShouldMatchers._
import net.aerospaceresearch.test.jplparser.data.{ExampleHeaderFile, Ascp1950TestData}

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

    it("calculates the chebyshev position polynoms") {
      val calculator = service.positionPolynomVariables(-1) _
      assert(BigDecimal(1.0) === calculator(0))
      assert(BigDecimal(-1.0) === calculator(1))
      assert(BigDecimal(-1.0) === calculator(13))

    }

    it("returns the position vector") {
      val inKm = (
         3.538040100792197E7,
         -4.591161252921398E7,
         -2.8228388144543815E7
      )

      val expectedPosition = (
        fromKm(inKm._1),
        fromKm(inKm._2),
        fromKm(inKm._3)
      )

      val position = service.position(Mercury, 2433264.5)

      assert(position._1.toDouble === expectedPosition._1.toDouble)
      assert(position._2.toDouble === expectedPosition._2.toDouble)
      assert(position._3.toDouble === expectedPosition._3.toDouble)
    }

    it("returs the chebyshev velocity polynoms") {
      val calculator = service.velocityPolynomVariables(-1) _

      assert(BigDecimal(-4) === calculator(2))
      assert(BigDecimal(-64) === calculator(8))
      assert(BigDecimal(169) === calculator(13))
    }

    it("returns the velocity vector") {
      val inKmPerDay = (
        2679252.174950161,
        2324167.119903723,
        963016.6493773247
      )

      val expectedVelocity = (
        fromKmPerDay(inKmPerDay._1),
        fromKmPerDay(inKmPerDay._2),
        fromKmPerDay(inKmPerDay._3)
      )

      val velocity = service.velocity(Mercury, 2433264.5)

      assert(velocity._1.toDouble === expectedVelocity._1.toDouble, "x mismatches")
      velocity._2.toDouble should be (expectedVelocity._2.toDouble plusOrMinus 1e-11)
      assert(velocity._3.toDouble === expectedVelocity._3.toDouble, "z mismatches")
    }


  }

}
