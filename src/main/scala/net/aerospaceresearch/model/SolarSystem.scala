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

package net.aerospaceresearch.model

import net.aerospaceresearch.jplparser.Types._
import scala.collection.parallel.immutable.ParSeq

/**
 * This class presents a solar system to a given point in time
 * User: Elmar Athmer
 * Date: 17.06.13
 * Time: 23:34
 */
case class SolarSystem(bodies: ParSeq[Body], centerMass: Body, time: JulianTime) {
  /**
   * default leapsize is 1 second
   */
  val defaultLeap = 1.0 / (24 * 60 * 60)

  val allBodies = bodies :+ centerMass

  def nextStep(leap: Double = defaultLeap): SolarSystem =
    SolarSystem(bodies.map(_.nextStep(this, leap)), centerMass, time + leap)

  /**
   * TODO: write unit test
   * @param time
   * @param resultEvery
   * @param leap
   * @return
   */
  def goto(time: JulianTime, resultEvery: Double = 1, leap: Double = defaultLeap): List[SolarSystem] = {
    def iter(systems: List[SolarSystem], cur: SolarSystem, count: Double): List[SolarSystem] =
      if(cur.time > time) cur :: systems
      else if(count >= resultEvery) iter(cur :: systems, cur.nextStep(leap), 0)
      else iter(systems, cur.nextStep(leap), count + leap)

    iter(Nil, this, 0.0)
  }
}
