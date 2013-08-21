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
  def velocity(v0: DenseVector[Double], a: DenseVector[Double], δt: Double): DenseVector[Double] = {
    def f(velocity: DenseVector[Double], t: Double): DenseVector[Double] =
      velocity + a * δt

    rungeKutta4(f, v0, δt)
  }


  /**
   *
   * @param r0
   * @param v1
   * @param δt
   * @return
   */
  def position(r0: DenseVector[Double], v1: DenseVector[Double], δt: Double): DenseVector[Double] = {
    def f(pos: DenseVector[Double], t: Double): DenseVector[Double] =
      pos + v1 * δt

    rungeKutta4(f, r0, δt)
  }

  /**
   *
   * @param f derivative function
   * @param x0 initial value at beginning of the interval, i.e. at t0
   * @param δt step size
   * @return x1: value of x at t1
   */
  def rungeKutta4(f: Derivative[Double], x0: DenseVector[Double], δt: Double): DenseVector[Double] = {
    val ka: DenseVector[Double] = f(x0, 0)
    val kb = f(x0 + ka * (δt / 2), δt / 2)
    val kc = f(x0 + (kb * (δt / 2)), δt / 2)
    val kd = f(x0 + kc * δt, δt)

    val k = (ka + (kb + kc) * 2.0 + kd) / 6.0

    x0 + k * δt
  }

}
