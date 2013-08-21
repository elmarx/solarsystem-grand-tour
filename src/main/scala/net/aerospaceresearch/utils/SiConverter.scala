package net.aerospaceresearch.utils

/**
 * Created by elmar on 21.08.13.
 */
object SiConverter {

  def fromKmPerDay(unit: BigDecimal): BigDecimal = {
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
  def fromKm(unit: BigDecimal): BigDecimal = {
    unit * kilo
  }

  /**
   * converts to si unit seconds
   * @param unit time in day
   * @return
   */
  def fromDay(unit: BigDecimal): BigDecimal = {
    // a day has 24 hours
    fromHour(24 * unit)
  }

  /**
   * converts hours to seconds
   * @param unit time in hours
   * @return seconds
   */
  def fromHour(unit: BigDecimal): BigDecimal = {
    fromMinute(60 * unit)
  }

  /**
   * converts minutes to seconds
   * @param unit time in minutes
   * @return seconds
   */
  def fromMinute(unit: BigDecimal): BigDecimal = {
    // one minute has 60 seconds
    unit * 60
  }
}
