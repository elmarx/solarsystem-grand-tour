package net.aerospaceresearch.units

import net.aerospaceresearch.utils.SiConverter

/**
 * Created by elmar on 28.08.13.
 */
case class Days(value: Double) extends AnyVal {

  def toS: Seconds = Seconds(SiConverter.fromDay(value).toDouble)

  def +(summand: Days): Days = Days(summand.value + value)

  def <=(that: Days): Boolean = value <= that.value
  def >(that: Days): Boolean = value > that.value
  def -(that: Days): Days = Days(value - that.value)

  def /(divisor: Days): Double = value / divisor.value
}

object Days {
  implicit def fromSecondsToDays(s: Seconds): Days = s.toDays
}

