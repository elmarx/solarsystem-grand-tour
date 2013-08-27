package net.aerospaceresearch.math

import org.apache.commons.math3.ode.FirstOrderDifferentialEquations
import breeze.linalg.DenseVector

/**
 * Created by elmar on 27.08.13.
 */
class VelocityOde(val a: DenseVector[Double]) extends FirstOrderDifferentialEquations {
  def getDimension: Int = 3

  def computeDerivatives(t: Double, y: Array[Double], yDot: Array[Double]) {
    yDot(0) = y(0) + a(0) * t
    yDot(1) = y(1) + a(1) * t
    yDot(2) = y(2) + a(2) * t
  }
}
