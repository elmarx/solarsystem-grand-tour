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
 * test data taken from ftp://ssd.jpl.nasa.gov/pub/eph/planets/ascii/de423/header.423
 */
trait TripletData {
  val tripletGroup =
    """
      |   1050
      |
      |     3   171   231   309   342   366   387   405   423   441   753   819   899
      |    14    10    13    11     8     7     6     6     6    13    11    10    10
      |     4     2     2     1     1     1     1     1     1     8     2     4     4
    """.stripMargin
}
