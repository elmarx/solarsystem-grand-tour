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

import scala.collection.parallel.immutable.ParSeq
import net.aerospaceresearch.units.{Seconds, Days}

/**
 * This class presents a solar system to a given point in time
 * User: Elmar Athmer
 * Date: 17.06.13
 * Time: 23:34
 */
case class SolarSystem(bodies: ParSeq[Body], centerMass: Star, time: Days) {
  /**
   * default leapsize is 1 second
   */
  val defaultLeap = Seconds(1)

  val allBodies = bodies :+ centerMass

  def nextStep(leap: Seconds = defaultLeap): SolarSystem =
    SolarSystem(bodies.map(_.nextStep(this, leap)), centerMass, time + leap)

  /**
   * TODO: write unit test
   * @param destinationTime
   * @param resultEvery
   * @param leap
   * @return
   */
  def goto(destinationTime: Days, resultEvery: Days = Days(1), leap: Seconds = defaultLeap): List[SolarSystem] = {
    // we should output every `resultEvery`, so we have to do `leapsPerOutput` leaps/iterations until we add the
    // current system to the outputlist
    val leapsPerOutput = (resultEvery / leap).toInt

    def iter(systems: List[SolarSystem], cur: SolarSystem, count: Int): List[SolarSystem] =
      if(cur.time >= destinationTime) cur :: systems
      else if(count == leapsPerOutput) iter(cur :: systems, cur.nextStep(leap), 0)
      else iter(systems, cur.nextStep(leap), count + 1)

    iter(Nil, this, 0)
  }
}
