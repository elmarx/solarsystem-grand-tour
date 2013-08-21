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

package net.aerospaceresearch.test.jplparser.data

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
