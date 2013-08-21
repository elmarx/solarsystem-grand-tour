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
import net.aerospaceresearch.jplparser.DataReader

class DataReaderSpec extends  FunSpec {

  describe("The datareader") {

    it("returns the filename to read") {
      assert(new DataReader().getFilenameForJulianTime(2456497) === "ascp2000.423")

    }

    it("generates a solarsystem") {
      val system = new DataReader().system()
      assert(system.centerMass.mass === BigDecimal("1.988435E30").toDouble)
      assert(system.bodies.length === 9)
    }
  }

}
