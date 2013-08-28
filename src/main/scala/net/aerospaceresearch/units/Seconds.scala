package net.aerospaceresearch.units

/**
 * Created by elmar on 28.08.13.
 */
case class Seconds(value: Double) {

  def toDays: Days = Days(value / (24 * 60 * 60))

  def /(divisor: Seconds) = value / divisor.value

}
