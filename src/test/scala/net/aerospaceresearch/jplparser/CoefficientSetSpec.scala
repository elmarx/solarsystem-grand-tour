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

class CoefficientSetSpec extends FunSpec {

  describe("the CoefficientSet Factory") {

    it("generates a CoefficentSet for a list of records") {
      val data = List(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12).map(BigDecimal(_))

      val expectedResult = List(
        (BigDecimal(1), BigDecimal(5), BigDecimal(9)),
        (BigDecimal(2), BigDecimal(6), BigDecimal(10)),
        (BigDecimal(3), BigDecimal(7), BigDecimal(11)),
        (BigDecimal(4), BigDecimal(8), BigDecimal(12))
      )

      assert(expectedResult === CoefficientSet(data, 4).coefficients)
    }
  }

}
