package net.aerospaceresearch.math

import org.apache.commons.math3.ode.FirstOrderDifferentialEquations
import breeze.linalg.DenseVector

/**
 * Ordinary differential equation for calculating the velocity
 * @param a acceleration
 */
class VelocityOde(val a: DenseVector[Double]) extends FirstOrderDifferentialEquations {
  def getDimension: Int = 3

  def computeDerivatives(t: Double, y: Array[Double], v1: Array[Double]) {
    v1(0) = a(0)
    v1(1) = a(1)
    v1(2) = a(2)
  }
}

object VelocityOde {
  def apply(a: DenseVector[Double]) = new VelocityOde(a)
}
