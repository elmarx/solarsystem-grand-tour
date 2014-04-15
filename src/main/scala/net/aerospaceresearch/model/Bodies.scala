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

/**
 * a star
 * @param name
 * @param mass
 * @param r0
 * @param v0
 */
case class Star(name: String, mass: Double, r0: DenseVector[Double], v0: DenseVector[Double]) extends Body {
  def leapedBody(r1: DenseVector[Double], v1: DenseVector[Double]): Body = Star(name, mass, r1, v1)
}

/**
 * a planet
 * @param name
 * @param mass
 * @param r0 position m
 * @param v0 velocity in m/s
 */
case class Planet(name: String, mass: Double, r0: DenseVector[Double], v0: DenseVector[Double]) extends Body {
  def leapedBody(r1: DenseVector[Double], v1: DenseVector[Double]): Body = Planet(name, mass, r1, v1)
}

/**
 *
 * @param name
 * @param mass
 * @param r0
 * @param v0
 */
case class Probe(name: String, mass: Double, r0: DenseVector[Double], v0: DenseVector[Double]) extends Body {
  def leapedBody(r1: DenseVector[Double], v1: DenseVector[Double]): Body = Probe(name, mass, r1, v1)
}
