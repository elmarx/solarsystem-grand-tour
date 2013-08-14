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


/**
 * TODO: add documentation
 *
 * User: Elmar Athmer
 * Part of: solarsystem-grand-tour
 */
object Formulas {

  type Derivative[T] = (DenseVector[T], JulianTime) => DenseVector[T]

  // gravitational constant (km^3/kg/s^2)
  val G: Double = 6.67259e-20 // G taken from "orbitbook"
  // val G: Double = 6.67384e-20 // G according to wikipedia

  val defaultδt = 1.0 / (24 * 60 * 60) / 1 // one second

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
   * @param bodies
   * @return
   */
  def acceleration(a: Body, bodies: List[Body]): DenseVector[Double] =
    a.forcesExperienced(bodies).reduce(_ + _) / a.mass

  /**
   *
   * @param a
   * @param bodies
   * @param δt
   * @return
   */
  def velocity(a: Body, bodies: List[Body], δt: Double = defaultδt): DenseVector[Double] = {
    def f(velocity: DenseVector[Double], t: Double): DenseVector[Double] =
      velocity + a.acceleration(bodies) * t

    rungeKutta4(f, a.v0, δt)
  }

  /**
   *
   * @param a
   * @param bodies
   * @param δt
   * @return
   */
  def position(a: Body, bodies: List[Body], δt: Double = defaultδt): DenseVector[Double] = {
    def f(pos: DenseVector[Double], t: Double): DenseVector[Double] =
      pos + a.v0 * t

    rungeKutta4(f, a.r0, δt)
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
