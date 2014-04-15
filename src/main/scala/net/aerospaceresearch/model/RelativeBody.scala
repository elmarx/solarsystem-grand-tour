/*
 * Copyright (c) 2014 Elmar Athmer
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

import breeze.linalg.DenseVector
import net.aerospaceresearch.math.Formulas

/**
 * Helper class to calculate absolute positions for a body given relative to another body
 */
case class RelativeBody(body: Body, relativeTo: String) {

  /**
   * generates a (absolutely positioned) body, relative to the specified system/body
   *
   * @param availableBodies list of available bodies, to find
   * @return
   */
  def resolve(availableBodies: Seq[Body]): Body = {
    val referenceSystem = availableBodies.find(_.name == relativeTo).getOrElse(throw new Exception("Unknown " +
      "body/system " + relativeTo))

    val absolutePosition = Formulas.absolutePosition(referenceSystem.r0, body.r0)

    // TODO: this could probably be much nicer with reflection or similar
    body match {
      case Star(name, mass, _, v0) => Star(name, mass, absolutePosition, v0)
      case Planet(name, mass, _, v0) => Planet(name, mass, absolutePosition, v0)
      case Probe(name, mass, _, v0) => Probe(name, mass, absolutePosition, v0)
      case _ => throw new Exception("Unknown body type. Please update implementation.")
    }
  }



}
