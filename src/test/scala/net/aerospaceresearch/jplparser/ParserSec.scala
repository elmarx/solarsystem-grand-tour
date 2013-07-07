package net.aerospaceresearch.jplparser

import org.scalatest.{FunSpec, FunSuite}
import org.scalatest.matchers.ShouldMatchers
import JplParser._
import net.aerospaceresearch.jplparser.data.{TripletData, ConstantGroupsData}

/**
 * Specification for the JPL Parser
 */
class ParserSec extends FunSpec {

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

    it("should read the first triplet") {
      new TripletData {
        expectResult((3, 14, 4)) {
          JplParser.parseTriplets(tripletGroup).head
        }
      }
    }

    it("should read the last triplet") {
      new TripletData {
        expectResult((899, 10, 4)) {
          JplParser.parseTriplets(tripletGroup).last
        }
      }
    }

    it("should calculate the number of records based on the parsed triplet") {
      new TripletData {
        expectResult(1016) {
          numberOfRecordsPerInterval(parseTriplets(tripletGroup))
        }
      }
    }

    it("should normalize strings") {
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


    it("should parse the timing data") {
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
  }
}
