package net.aerospaceresearch.utils

/**
 * Created by elmar on 21.08.13.
 */
object SiConverter {

  def fromKmPerDay(unit: Double): Double = {
    val m = fromKm(1)
    val s = fromDay(1)

    (unit * m) / s
  }



  val kilo = 1e3

  /**
   * TODO: the type could probably somehow be expressed as scala generic.
   * @param unit
   * @return
   */
  def fromKm(unit: Double): Double = {
    unit * kilo
  }

  /**
   * converts to si unit seconds
   * @param unit time in day
   * @return
   */
  def fromDay(unit: Double): Double = {
    // a day has 24 hours
    fromHour(24 * unit)
  }

  /**
   * converts hours to seconds
   * @param unit time in hours
   * @return seconds
   */
  def fromHour(unit: Double): Double = {
    fromMinute(60 * unit)
  }

  /**
   * converts minutes to seconds
   * @param unit time in minutes
   * @return seconds
   */
  def fromMinute(unit: Double): Double = {
    // one minute has 60 seconds
    unit * 60
  }
}
