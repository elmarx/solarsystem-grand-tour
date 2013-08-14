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

import net.aerospaceresearch.math
import breeze.linalg.DenseVector
import net.aerospaceresearch.math.Formulas
import net.aerospaceresearch.jplparser.EntityAssignments.AstronomicalObjects

/**
 * A body represents a planet, a probe, a moon, any particle etc.
 *
 * User: Elmar Athmer
 * Date: 17.06.13
 * Time: 23:37
 *
 * @param r0 position, as vector
 * @param mass the mass of the
 * @param v0 velocity
 */
case class Body(identity: AstronomicalObjects.Value, mass: Double, r0: DenseVector[Double], v0: DenseVector[Double]) {

  // def accelerate - Beschleunigen oder bremsen mit eigenem Antrieb

  def forcesExperienced(otherBodies: List[Body]): List[DenseVector[Double]] = {
    // f = [G * m1 * m2 % (R2 - R1)] / ||R2 - R1||^3
    val formula = Formulas.gravitationalForces(this) _
    otherBodies.filterNot(_ == this) map formula
  }

  def acceleration(otherBodies: List[Body]): DenseVector[Double] = Formulas.acceleration(this, otherBodies)

  def v1(otherBodies: List[Body]): DenseVector[Double] = Formulas.velocity(this, otherBodies)

  def r1(otherBodies: List[Body]): DenseVector[Double] =
    Formulas.position(this, otherBodies)

}
