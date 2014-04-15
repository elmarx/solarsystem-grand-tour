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
import scala.collection.parallel.immutable.ParSeq
import org.apache.commons.math3.ode.nonstiff.{ClassicalRungeKuttaIntegrator, RungeKuttaIntegrator}
import net.aerospaceresearch.units.Seconds


/**
 * TODO: add documentation
 *
 * User: Elmar Athmer
 * Part of: solarsystem-grand-tour
 */
object Formulas {

  // The gravitational constant in m^3/(kg*s2) from wikipedia
  val G = 6.67384e-11

  /**
   * gravitational forces a body b induces on body a
   * @param a body to calculate the affecting forces for
   * @param b body affecting body
   * @return
   */
  def gravitationalForces(a: Body)(b: Body): DenseVector[Double] =
    (b.r0 - a.r0) * (G * a.mass * b.mass) / BigDecimal(norm(b.r0 - a.r0)).pow(3).toDouble

  /**
   * the acceleration is the sum of all forces, divided by the mass of the body a itself
   * @param a body the acceleration is applied to
   * @param otherBodies all bodies applieng a force tho the body
   * @return
   */
  def acceleration(a: Body, otherBodies: ParSeq[Body]): DenseVector[Double] =
    a.forcesExperienced(otherBodies).reduce(_ + _) / a.mass

  /**
   * calculate the velocity caused by acceleration
   * @param v0 starting velocity
   * @param a acceleration applied
   * @param δt number of seconds the velocity should by accelerated
   * @return
   */
  def velocity(v0: DenseVector[Double], a: DenseVector[Double], δt: Seconds): DenseVector[Double] = {
    val integrator = new ClassicalRungeKuttaIntegrator(δt.value)

    // array to hold the result
    val v1 = new Array[Double](3)

    integrator.integrate(VelocityOde(a), 0.0, v0.toArray, δt.value, v1)

    DenseVector[Double](v1)
  }

  /**
   *
   * @param r0
   * @param v1
   * @param δt
   * @return
   */
  def position(r0: DenseVector[Double], v1: DenseVector[Double], δt: Seconds): DenseVector[Double] = {
    val integrator = new ClassicalRungeKuttaIntegrator(δt.value)

    val r1 = new Array[Double](3)

    integrator.integrate(PositionOde(v1), 0.0, r0.toArray, δt.value, r1)

    DenseVector[Double](r1)
  }

  /**
   *
   * @param a the absolute origin of the reference system
   * @param b the position of a point referring to a
   * @return
   */
  def absolutePosition(a: DenseVector[Double], b: DenseVector[Double]):
  DenseVector[Double]
  = a + b
}
