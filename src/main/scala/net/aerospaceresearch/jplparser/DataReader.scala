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

import org.joda.time.DateTimeUtils
import com.github.nscala_time.time.Imports._

/**
 * reads files based on the julian time given
 * This file makes some assumptions (i.e.: it's specific for the 423 ephemerides set)
 *
 */
object DataReader {
  val ephemeridesSet = 423
  val folderName = s"de$ephemeridesSet"
  val headerFile = s"header.$ephemeridesSet"

  /**
   * get the name of the ephemerides file for a given point in time
   * @param pointInTime a point in time in julian time
   * @return
   */
  def getFilenameForJulianTime(pointInTime: Types.JulianTime): String = {
    val epochMillis = DateTimeUtils.fromJulianDay(pointInTime)
    val year: Int = new DateTime(epochMillis).year.get
    val interval = year - (year % 50)

    s"ascp$interval.$ephemeridesSet"
  }


  /**
   * generate an EphemerisService for now (now as in "DateTime.now")
   * @return
   */
  def currentEphemerisService: EphemerisService = {
    // read header
    val headerSource = scala.io.Source.fromFile(s"$folderName/$headerFile")
    val headerContent = headerSource.getLines mkString "\n"
    headerSource.close()

    val dataFilename = getFilenameForJulianTime(DateTimeUtils.toJulianDay(DateTime.now.toInstant.getMillis))

    // read data
    val dataSource = io.Source.fromFile(s"$folderName/$dataFilename")
    val dataContent = dataSource.getLines mkString "\n"
    dataSource.close()


    JplParser.generateService(headerContent, dataContent)
  }



}
