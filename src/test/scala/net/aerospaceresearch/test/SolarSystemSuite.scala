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
import net.aerospaceresearch.model.{Body, Planet, Star, SolarSystem}
import net.aerospaceresearch.jplparser.DataReader
import net.aerospaceresearch.jplparser.Types._
import org.scalatest.Matchers._
import breeze.linalg.DenseVector
import scala.collection.parallel.immutable.ParSeq
import net.aerospaceresearch.units.{Seconds, Days}

/**
 * Created by elmar on 21.08.13.
 */
class SolarSystemSuite extends FunSuite with BeforeAndAfter {

  private val dataReader: DataReader = DataReader()

  test("can calculate a very simple example/test model") {
    val startTime = Days(0)
    val centerMass = Star("Sun", 2e30, DenseVector[Double](0, 0, 0), DenseVector[Double](0, 0, 0))
    val initialEarth = Planet("Earth", 6e24, DenseVector[Double](1.5e11, 0, 0), DenseVector[Double](0, 30e3, 0))
    val bodies = ParSeq[Body](initialEarth)

    val initialSystem = new SolarSystem(bodies, centerMass, startTime)

    val expectedPositionEarth = DenseVector[Double](149999999999.994, 30000, 0)
    val expectedVelocityEarth = DenseVector[Double](-0.005932302222222223, 30e3, 0)
    val expectedAccelerationEarth = DenseVector[Double](-0.005932302222222223, 0, 0)

    val calculatedSystem = initialSystem.nextStep(Seconds(1))
    val calculatedEarth = calculatedSystem.bodies(0)
    assert(initialEarth.acceleration(initialSystem.allBodies.filter(_ != initialEarth)) === expectedAccelerationEarth)


    assert(calculatedEarth.name === initialEarth.name)

    calculatedEarth.v0(0) should be (expectedVelocityEarth(0) +- 1e-5)
    calculatedEarth.v0(1) should be (expectedVelocityEarth(1) +- 1e-5)
    calculatedEarth.v0(2) should be (expectedVelocityEarth(2) +- 1e-5)

    calculatedEarth.r0(0) should be (expectedPositionEarth(0) +- 1e-4)
    calculatedEarth.r0(1) should be (expectedPositionEarth(1) +- 1e-4)
    calculatedEarth.r0(2) should be (expectedPositionEarth(2) +- 1e-4)
  }

  test("can calculate the movement and velocity after one day") {
    val startTime: Days = Days(2456520)
    val initialSystem = dataReader.system(startTime)

    val calculatedSystem = initialSystem.goto(startTime + Days(1))(0)
    val givenSystem = dataReader.system(startTime + Days(1))

    // now compare the calculatedSystem and the givenSystem
    calculatedSystem.time.value should be (givenSystem.time.value +- 1e-5)

    val mercuryCalculated = calculatedSystem.bodies.find(_.name == "Mercury").get
    val mercuryGiven = givenSystem.bodies.find(_.name == "Mercury").get

    mercuryCalculated.v0(0) should be (mercuryGiven.v0(0) +- 25)
    mercuryCalculated.r0(0) should be (mercuryGiven.r0(0) +- 1e6)
  }



}
