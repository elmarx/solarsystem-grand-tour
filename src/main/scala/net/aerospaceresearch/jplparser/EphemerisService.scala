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

package net.aerospaceresearch.jplparser

import Types._

class EphemerisService(quartets: List[(Int, Int, Int, Int)],
                       val entities: List[AstronomicalObject],
                       timingData: (JulianTime, JulianTime, Double),
                       constants: Map[String, BigDecimal]
                        ) {

  val start = timingData._1
  val end = timingData._2
  val intervalDuration = timingData._3

  /**
   * convert km to au, using the values from the header file, with a fallback value
   * @param value value in km to convert
   * @return
   */
  def inAu(implicit value: BigDecimal) = value / constants.get("AU").getOrElse(0.149597870699626200e+09)

  def inAu(implicit value: (BigDecimal, BigDecimal, BigDecimal)): (BigDecimal, BigDecimal, BigDecimal) =
    (inAu(value._1), inAu(value._2), inAu(value._3))


  /**
   * calculate the position of an entity for a given pointInTime
   * @param entityId id/exponent of the entity
   * @param pointInTime pointInTime in Julian Time
   * @return
   */
  def position(entityId: EntityAssignments.AstronomicalObjects.Value, pointInTime: JulianTime): Position = {
    val entity = this.entity(entityId)
    val coefficientSet = entity.intervals.find(_.includes(pointInTime)).get.sets(subInterval(entity, pointInTime))
    val time = chebyshevTime(entityId, pointInTime)

    val calculator = calculatePolynom(positionPolynomVariables(time)) _

    val x = calculator(coefficientSet.coefficients.map(_._1))
    val y = calculator(coefficientSet.coefficients.map(_._2))
    val z = calculator(coefficientSet.coefficients.map(_._3))

    (x, y, z)
  }

  def findCoefficientSet(entity: AstronomicalObject, pointInTime: JulianTime): CoefficientSet = {
    entity.intervals.find(_.includes(pointInTime)).get.sets(subInterval(entity, pointInTime))
  }

  def velocity(entityId: EntityAssignments.AstronomicalObjects.Value, pointInTime: JulianTime): Velocity = {
    val entity: AstronomicalObject = this.entity(entityId)

    val coefficientSet = findCoefficientSet(entity, pointInTime)
    val time = chebyshevTime(entityId, pointInTime)

    /*
     * The next line accounts for differentiation of the iterative
     * formula with respect to chebyshev time. Essentially, if dx/dt
     * = (dx/dct) times (dct/dt), the next line includes the factor
     * (dct/dt) so that the units are km/day.
     */
    def calculator(coefficients: List[BigDecimal]): BigDecimal =
      calculatePolynom(velocityPolynomVariables(time))(coefficients) * (2 * entity.numberOfCompleteSets / intervalDuration)


    val x = calculator(coefficientSet._1)
    val y = calculator(coefficientSet._2)
    val z = calculator(coefficientSet._3)

    (x, y, z)
  }

  /**
   * calculate any polynom by summing up the product of coefficients and variables
   * @param variableValue function, that provides the values for variables with a given exponent
   * @param coefficients list of coefficients to use for calculation
   * @return
   */
  def calculatePolynom(variableValue: (Int) => BigDecimal)(coefficients: List[BigDecimal]) = {
    coefficients.zipWithIndex.map {
      case (coefficient, index) => coefficient * variableValue(index)
    }.sum
  }

  /**
   * finds the exponent of the subInterval (i.e.: set of coefficients) responsible for the given point in Time
   * @param entity entity with intervals and coefficient sets
   * @param pointInTime a point in time for which the subInterval Index should by found
   * @return
   */
  def subInterval(entity: AstronomicalObject, pointInTime: JulianTime): Int = {
    val interval = entity.intervals.find(_.includes(pointInTime)).get

    ((pointInTime - interval.startingTime) / interval.subIntervalDuration).toInt
  }

  /**
   * calculate the chebyshev coefficient corresponding to the time
   * @param entityId id of the entity to calculate the chebyshev time coefficient
   * @param pointInTime the desired point in time in julian time
   * @return
   */
  def chebyshevTime(entityId: EntityAssignments.AstronomicalObjects.Value, pointInTime: JulianTime): Double = {
    val interval = entity(entityId).intervals.find(_.includes(pointInTime)).get

    val subInterval = this.subInterval(entity(entityId), pointInTime)
    val intervalStartTime = interval.startingTime
    val subIntervalDuration = interval.subIntervalDuration

    2 * ((pointInTime - (subInterval * subIntervalDuration + intervalStartTime)) / subIntervalDuration) - 1
  }

  /**
   * find an entity in the list of entities
   * @param entityId id of the entity to find
   * @return
   */
  def entity(entityId: EntityAssignments.AstronomicalObjects.Value): AstronomicalObject = entities.find(_.id == entityId.id).get

  /**
   * calculate the chebychev coefficient for an exponent
   * @param chebyshevTime time of the position polynom to calculate
   * @param exponent exponent to calculate for
   * @return
   */
  def positionPolynomVariables(chebyshevTime: BigDecimal)(exponent: Int): BigDecimal = {
    val cheby = positionPolynomVariables(chebyshevTime) _

    exponent match {
      case 0 => 1
      case 1 => chebyshevTime
      case _ => 2 * chebyshevTime * cheby(exponent - 1) - cheby(exponent - 2)
    }
  }

  /**
   *
   * @param chebyshevTime
   * @param exponent
   * @return
   */
  def velocityPolynomVariables(chebyshevTime: BigDecimal)(exponent: Int): BigDecimal = {
    val velocity = velocityPolynomVariables(chebyshevTime) _
    val position = positionPolynomVariables(chebyshevTime) _

    exponent match {
      case 0 => 0
      case 1 => 1
      case 2 => 4 * chebyshevTime
      case _ => 2 * chebyshevTime * velocity(exponent - 1) + 2 * position(exponent - 1) - velocity(exponent - 2)
    }
  }
}
