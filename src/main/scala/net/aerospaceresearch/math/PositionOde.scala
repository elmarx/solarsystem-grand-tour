package net.aerospaceresearch.math

import breeze.linalg.DenseVector
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations

/**
 * Created by elmar on 27.08.13.
 */
class PositionOde(val v: DenseVector[Double]) extends FirstOrderDifferentialEquations {
  def getDimension: Int = 3

  def computeDerivatives(t: Double, y: Array[Double], r1: Array[Double]) {
    r1(0) = v(0)
    r1(1) = v(1)
    r1(2) = v(2)
  }
}

object PositionOde {
  def apply(v: DenseVector[Double]) = new PositionOde(v)
}
