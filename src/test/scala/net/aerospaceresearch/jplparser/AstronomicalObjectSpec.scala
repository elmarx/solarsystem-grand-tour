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
import net.aerospaceresearch.jplparser.data.{Ascp1950TestData, TripletData}
import net.aerospaceresearch.jplparser.JplParser._

class AstronomicalObjectSpec extends FunSpec {

  lazy val entities = new TripletData with Ascp1950TestData {
    val x = listOfAstronomicalObjects(parseTriplets(tripletGroup), parseDataRecordsAsList(content, 1016))
  }.x

  describe("the Entity") {

    it("extracts the records belonging to itself from the complete list") {
      val mercury = entities(0)
      assert(mercury.records.size === 168 * 3)
      assert(mercury.records(0) === BigDecimal("4.416951494022430000e+07"))
      assert(mercury.records(168) === BigDecimal("-2.689229672372822000e+07"), "first record in the second interval")
    }
  }
}
