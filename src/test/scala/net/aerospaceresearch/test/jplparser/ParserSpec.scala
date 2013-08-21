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

import org.scalatest.{FunSpec, FunSuite}
import net.aerospaceresearch.jplparser.JplParser._
import net.aerospaceresearch.test.jplparser.data.{ExampleHeaderFile, Ascp1950TestData, TripletData, ConstantGroupsData}
import net.aerospaceresearch.jplparser.{EntityAssignments, Interval, CoefficientSet, JplParser}

/**
 * Specification for the JPL Parser
 */
class ParserSpec extends FunSpec {

  describe("The JPL Parser") {

    it("should convert JPL style decimals to BigDecimal") {
      expectResult(BigDecimal("0.216336838636074100E-01")) {
        JplParser.parseDecimal("0.216336838636074100D-01")
      }
    }

    it("should read the constant groups into a Map") {
      new ConstantGroupsData {

        expectResult(referenceMap) {
          JplParser.parseConstantGroups(rawNamesWithLeadingWhitespace, rawValuesWithLeadingWhitespace)
          JplParser.parseConstantGroups(rawNames, rawValues)
        }
      }
    }

    it("reads the triplets") {
      new TripletData {
        expectResult((3, 14, 4))(JplParser.parseTriplets(tripletGroup).head)
        expectResult((899, 10, 4))(JplParser.parseTriplets(tripletGroup).last)
      }
    }

    it("augments triplets to be quartets") {
      new TripletData {
        expectResult((3, 14, 4, 3))(JplParser.parseQuartets(tripletGroup).head)
        expectResult((819, 10, 4, 2))(JplParser.parseQuartets(tripletGroup)(11))
        expectResult((899, 10, 4, 3))(JplParser.parseQuartets(tripletGroup).last)
      }
    }

    it("calculates the number of records based on the parsed triplet") {
      new TripletData {
        expectResult(1016) {
          recordsPerInterval(parseQuartets(tripletGroup))
        }
      }
    }

    it("normalizes strings") {
      val multiline =
        """
          |    a
          |    b    c d
          |e f
          |g   h  i
          |
          |j
          |
          |
          |
          |k
        """.stripMargin
      expectResult("a b c d e f g h i j k") {
        normalize(multiline)
      }
    }


    it("parses the timing data") {
      val group1030 =
        """
          |
          |   1030
          |
          |  2378480.50  2524624.50         32.
          |
          |
        """.stripMargin

      expectResult((2378480.50, 2524624.50, 32.0)) {
        parseTimingData(group1030)
      }
    }

    it("knows how many entries to drop at the end of an interval") {
      expectResult(2)(numberOfTrailingEntries(1016))
    }


    it("groups intervals and filters out the garbage elements") {
      val expectedResult = List(
        List("start1", "end1", "1", "2", "3", "4", "4"),
        List("start2", "end2", "5", "6", "7", "8", "8"),
        List("start3", "end3", "9", "10", "11", "12", "12")
      )

      expectResult(expectedResult) {
        groupOfIntervals(List(
          "index1", "number", "start1", "end1", "1", "2", "3", "4", "4", "empty", "empty",
          "index2", "number", "start2", "end2", "5", "6", "7", "8", "8", "empty", "empty",
          "index3", "number", "start3", "end3", "9", "10", "11", "12", "12", "empty", "empty"
        ), 5)
      }
    }

    it("extracts intervals for entities") {
      val timing = BigDecimal(0.5)
      val x = BigDecimal(1)
      val y = BigDecimal(2)
      val z = BigDecimal(3)
      val other = BigDecimal(9)

      val intervals = List(List(timing, timing, x, x, x, y, y, y, z, z, z, other, other, other))

      val expected = Interval(timing.toDouble, timing.toDouble, List(
        CoefficientSet(List(
          (x, y, z),
          (x, y, z),
          (x, y, z)
        ))
      ))

      expectResult(List(expected)) {
        extractIntervalsForEntity((3, 3, 1, 3), intervals)
      }
    }

    it("parses data records to AstronomicalObjects") {
      new Ascp1950TestData with TripletData {
        val quartets = parseQuartets(tripletGroup)

        val result = parseDataRecords(content, quartets)

        val mercury = result(0)
        assert(mercury.id === EntityAssignments.AstronomicalObjects.Mercury.id)
        assert(mercury.intervals.size === 3)

        assert(mercury.intervals(0).startingTime === 2.433264500000000000e+06)
        assert(mercury.intervals(0).endingTime === 2.433296500000000000e+06)

        assert(mercury.intervals(2).startingTime === 2.433328500000000000e+06)
        assert(mercury.intervals(2).endingTime === 2.433360500000000000e+06)

        assert(mercury.intervals(0).sets.size === 4)

        val uranus = result(6)
        assert(uranus.id === EntityAssignments.AstronomicalObjects.Uranus.id)

        assert(uranus.intervals(2).startingTime === mercury.intervals(2).startingTime)

        assert(uranus.intervals(0).sets(0).coefficients(0) === (
          BigDecimal("-1.844007491629836E8"),
          BigDecimal("2.590029055671577E9"),
          BigDecimal("1.136985853558836E9")
        ))
      }
    }

    it("generates the EphemerisService based on the header/content given") {
      new Ascp1950TestData with ExampleHeaderFile {
        val service = generateService(headerContent, content)
      }
    }
  }
}
