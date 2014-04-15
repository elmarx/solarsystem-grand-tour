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

package net.aerospaceresearch.jplparser

import Types._

case class CoefficientSet(coefficients: List[Coefficient]) {

  val _1 : List[BigDecimal] = coefficients.map(_._1)
  val _2 : List[BigDecimal] = coefficients.map(_._2)
  val _3 : List[BigDecimal] = coefficients.map(_._3)
}

object CoefficientSet {

  /**
   * factory method to create a CoefficientSet based on a flat list of coefficients,
   * and the number of coefficients in the set
   * @param coefficients flat list of all coefficients, all components
   * @param xCoefficients the number of coefficients per component
   * @return
   */
  def apply(coefficients: List[BigDecimal], xCoefficients: Int): CoefficientSet =
    CoefficientSet(
      (
        coefficients.slice(0, xCoefficients),
        coefficients.slice(xCoefficients, xCoefficients * 2),
        coefficients.slice(xCoefficients * 2, xCoefficients * 3)
      ).zipped.toList
    )


}