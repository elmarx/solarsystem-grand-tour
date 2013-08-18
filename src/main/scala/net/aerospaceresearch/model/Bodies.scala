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
 * @param r0
 * @param v0
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