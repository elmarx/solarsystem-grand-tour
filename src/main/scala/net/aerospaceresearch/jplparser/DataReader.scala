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
import EntityAssignments.AstronomicalObjects._
import net.aerospaceresearch.model.{Body, SolarSystem}
import breeze.linalg.DenseVector

/**
 * reads files based on the julian time given
 * This file makes some assumptions (i.e.: it's specific for the 423 ephemerides set)
 *
 */
object DataReader {
  def current() = new DataReader(DateTimeUtils.toJulianDay(DateTime.now.toInstant.getMillis))
}

class DataReader(val pointInTime: Double) {
  lazy val masses = Map(
    Mercury -> BigDecimal("3.3022E23"),
    Venus -> BigDecimal("4.869E24"),
    Earth_Moon_Barycenter -> BigDecimal("6.045678E24"),
    Mars -> BigDecimal("6.4191E23"),
    Jupiter -> BigDecimal("1.8988E27"),
    Saturn -> BigDecimal("5.685E26"),
    Uranus -> BigDecimal("8.6625E25"),
    Neptune -> BigDecimal("1.0278E26"),
    Pluto -> BigDecimal("1.314E22"),
    Moon_Geocentric -> BigDecimal("7.3459E22"),
    Sun -> BigDecimal("1.988435E30")
  )

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
   * generate an EphemerisService for the pointInTime
   * @return
   */
  lazy val ephemerisService: EphemerisService = JplParser.generateService(headerContent, currentDataContent)

  lazy val headerContent = {
    val headerSource = io.Source.fromFile(s"$folderName/$headerFile")
    val headerContent = headerSource.getLines mkString "\n"
    headerSource.close()
    headerContent
  }

  lazy val currentDataContent = {
    val dataFilename = getFilenameForJulianTime(pointInTime)

    // read data
    val dataSource = io.Source.fromFile(s"$folderName/$dataFilename")
    val dataContent = dataSource.getLines mkString "\n"
    dataSource.close()
    dataContent
  }

  def system: SolarSystem = {
    val sun = Body(Sun, masses(Sun).toDouble, DenseVector(0, 0, 0), DenseVector(0, 0, 0))
    val bodies = masses.filter{
      case(Sun, _) => false
      case(Moon_Geocentric,_ ) => false
      case _ => true
    }.map { case (entity, _) => entity } map toBody

    new SolarSystem(bodies.toList.par, sun, pointInTime)
  }

  def toBody(entity: Value): Body = {
    val (rX, rY, rZ) = ephemerisService.position(entity, pointInTime)
    val (vX, vY, vZ) = ephemerisService.velocity(entity, pointInTime)

    Body(
      entity,
      masses(entity).toDouble,
      DenseVector(rX.toDouble, rY.toDouble, rZ.toDouble),
      DenseVector(vX.toDouble, vY.toDouble, vZ.toDouble)
    )
  }
}
