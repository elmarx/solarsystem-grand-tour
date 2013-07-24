package net.aerospaceresearch.jplparser

/**
 * These are constants that might change with the version of the ephemerid-files
 */
object EntityAssignments {

  object AstronomicalObjects extends Enumeration {
    val Mercury, Venus, Earth_Moon_Barycenter, Mars, Jupiter, Saturn,
    Uranus, Neptune, Pluto, Moon_Geocentric, Sun = Value
  }

  val Nutations = 11
  val Librations = 12
}
