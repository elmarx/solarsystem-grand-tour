package net.aerospaceresearch.jplparser.data

/**
 * Test data taken from ftp://ssd.jpl.nasa.gov/pub/eph/planets/ascii/de423/header.423
 */
trait ConstantGroupsData {

  val rawNames =
    """1040
      |
      |   222
      |  DENUM   LENUM   TDATEF
      |  TDATEB  CENTER  CLIGHT
    """.stripMargin
  val rawValues =
    """1041
      |
      |   222
      |  0.423000000000000000D+03  0.423000000000000000D+03  0.201002051148400000D+14
      |  0.201002051137180000D+14  0.110000000000000000D+02  0.299792458000000000D+06
      |
    """.stripMargin

  val rawNamesWithLeadingWhitespace =
    """                    1040
      |
      |   222
      |  DENUM   LENUM   TDATEF
      |  TDATEB  CENTER  CLIGHT
    """.stripMargin
  val rawValuesWithLeadingWhitespace =
    """
      |1041
      |
      |   222
      |  0.423000000000000000D+03  0.423000000000000000D+03  0.201002051148400000D+14
      |  0.201002051137180000D+14  0.110000000000000000D+02  0.299792458000000000D+06
      |
    """.stripMargin

  val referenceMap = Map(
    "DENUM" -> BigDecimal("0.423000000000000000E+03"),
    "LENUM" -> BigDecimal("0.423000000000000000E+03"),
    "TDATEF" -> BigDecimal("0.201002051148400000E+14"),
    "TDATEB" -> BigDecimal("0.201002051137180000E+14"),
    "CENTER" -> BigDecimal("0.110000000000000000E+02"),
    "CLIGHT" -> BigDecimal("0.299792458000000000E+06")
  )
}
