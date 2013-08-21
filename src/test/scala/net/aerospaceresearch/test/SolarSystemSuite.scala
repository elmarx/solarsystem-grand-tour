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

package net.aerospaceresearch.test

import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter
import net.aerospaceresearch.model.SolarSystem
import net.aerospaceresearch.jplparser.DataReader
import net.aerospaceresearch.jplparser.Types._
import org.scalatest.matchers.ShouldMatchers._

/**
 * Created by elmar on 21.08.13.
 */
class SolarSystemSuite extends FunSuite with BeforeAndAfter {

  private val dataReader: DataReader = DataReader()

  test("can calculate the movement and velocity after one day") {
    val startTime: JulianTime = 2456520
    val initialSystem = dataReader.system(startTime)

    val calculatedSystem = initialSystem.goto(startTime + 1)(0)
    val givenSystem = dataReader.system(startTime + 1)

    // now compare the calculatedSystem and the givenSystem
    calculatedSystem.time should be (givenSystem.time plusOrMinus 1e-5)

    val mercuryCalculated = calculatedSystem.bodies.find(_.name == "Mercury").get
    val mercuryGiven = givenSystem.bodies.find(_.name == "Mercury").get

    mercuryCalculated.v0(0) should be (mercuryGiven.v0(0) plusOrMinus 1e-5)
    mercuryCalculated.r0(0) should be (mercuryGiven.r0(0) plusOrMinus 1e-5)
  }

}
