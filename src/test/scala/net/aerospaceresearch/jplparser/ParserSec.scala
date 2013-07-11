package net.aerospaceresearch.jplparser

import org.scalatest.{FunSpec, FunSuite}
import org.scalatest.matchers.ShouldMatchers
import JplParser._
import net.aerospaceresearch.jplparser.data.{Ascp1950TestData, TripletData, ConstantGroupsData}

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

    it("reads the triplets") {
      new TripletData {
        expectResult((3, 14, 4))(JplParser.parseTriplets(tripletGroup).head)
        expectResult((899, 10, 4))(JplParser.parseTriplets(tripletGroup).last)
      }
    }

    it("calculates the number of records based on the parsed triplet") {
      new TripletData {
        expectResult(1016) {
          numberOfRecordsPerInterval(parseTriplets(tripletGroup))
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

    it("parses the testdata as a list") {
      new Ascp1950TestData {
        val list = parseDataRecordsAsList(content, 1016)
        assert(list(0) === BigDecimal("4.416951494022430000e+07"))
        assert(list(1015) === BigDecimal("-5.941518184249737000e-10"))
        assert(list(1016) === BigDecimal("-2.689229672372822000e+07"))

        assert(list(63) === BigDecimal("7.518925004544147000e-01"))
        assert(list(1842) === BigDecimal("4.177626931814747E-5"))
        assert(list(2525) === BigDecimal("-942.2572516376157"))

        assert(list.size === 3 * 1016)
      }
    }

    it("knows how many entries to drop at the end of an interval") {
      expectResult(2)(numberOfTrailingEntries(1016))
    }
  }
}
