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

package net.aerospaceresearch.math

import net.aerospaceresearch.model.Body
import breeze.linalg._
import net.aerospaceresearch.jplparser.Types.JulianTime
import scala.collection.parallel.immutable.ParSeq
import org.apache.commons.math3.ode.nonstiff.{ClassicalRungeKuttaIntegrator, RungeKuttaIntegrator}


/**
 * TODO: add documentation
 *
 * User: Elmar Athmer
 * Part of: solarsystem-grand-tour
 */
object Formulas {

  type Derivative[T] = (DenseVector[T], JulianTime) => DenseVector[T]

  // The gravitational constant in m^3/(kg*s2) from wikipedia
  val G = 6.67384e-11

  /**
   *
   * @param a
   * @param b
   * @return
   */
  def gravitationalForces(a: Body)(b: Body): DenseVector[Double] =
    (b.r0 - a.r0) * (G * a.mass * b.mass) / BigDecimal(norm(b.r0 - a.r0)).pow(3).toDouble

  /**
   * the acceleration is the sum of all forces, divided by the mass of the body a itself
   * @param a
   * @param otherBodies
   * @return
   */
  def acceleration(a: Body, otherBodies: ParSeq[Body]): DenseVector[Double] =
    a.forcesExperienced(otherBodies).reduce(_ + _) / a.mass

  /**
   *
   * @param v0
   * @param a
   * @param δt
   * @return
   */
  def velocity(v0: DenseVector[Double], a: DenseVector[Double], δt: Double): DenseVector[Double] =
    v0 + a * δt


  /**
   *
   * @param r0
   * @param v1
   * @param δt
   * @return
   */
  def position(r0: DenseVector[Double], v1: DenseVector[Double], δt: Double): DenseVector[Double] =
    r0 + v1 * δt
}
