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
   * convert km to au, using the values from the header file, with a fallback
   * @param value value in km to convert
   * @return
   */
  def inAu(implicit value: BigDecimal) = value / constants.get("AU").getOrElse(0.149597870699626200e+09)

  def inAu(implicit value: (BigDecimal, BigDecimal, BigDecimal)): (BigDecimal, BigDecimal, BigDecimal) =
    (inAu(value._1), inAu(value._2), inAu(value._3))


  def position(entityId: EntityAssignments.AstronomicalObjects.Value, pointInTime: JulianTime): Position = {
    val entity = this.entity(entityId)
    val coefficientSet = entity.intervals.find(_.includes(pointInTime)).get.sets(subInterval(entity, pointInTime))
    val time = chebyshevTime(entityId, pointInTime)

    val calculator = calculatePositionComponent(chebychevAt((1, time), time)) _

    val x = calculator(coefficientSet.coefficients.map(_._1))
    val y = calculator(coefficientSet.coefficients.map(_._2))
    val z = calculator(coefficientSet.coefficients.map(_._3))

    inAu((x, y, z))
  }

  def calculatePositionComponent(positionPolynom: (Int) => BigDecimal)(coefficients: List[BigDecimal]) = {
    coefficients.zipWithIndex.map {
      case (coefficient, index) => coefficient * positionPolynom(index)
    }.sum
  }

  def subInterval(entity: AstronomicalObject, pointInTime: JulianTime): Int = {
    val interval = entity.intervals.find(_.includes(pointInTime)).get

    ((pointInTime - interval.startingTime) / interval.subIntervalDuration).toInt
  }

  def chebyshevTime(entityId: EntityAssignments.AstronomicalObjects.Value, pointInTime: JulianTime): Double = {
    val interval = entity(entityId).intervals.find(_.includes(pointInTime)).get

    val subInterval = this.subInterval(entity(entityId), pointInTime)
    val intervalStartTime = interval.startingTime
    val subIntervalDuration = interval.subIntervalDuration

    2 * ((pointInTime - (subInterval * subIntervalDuration + intervalStartTime)) / subIntervalDuration) - 1
  }

  def entity(entityId: EntityAssignments.AstronomicalObjects.Value) = entities.find(_.id == entityId.id).get

  def chebychevAt(initialValues: (BigDecimal, BigDecimal), start: BigDecimal)(position: Int): BigDecimal = {
    val cheby = chebychevAt(initialValues, start) _

    position match {
      case 0 => initialValues._1
      case 1 => initialValues._2
      case _ => 2 * start * cheby(position - 1) - cheby(position - 2)
    }
  }
}
