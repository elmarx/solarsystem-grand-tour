package net.aerospaceresearch.jplparser

import scala.collection.{immutable, Map}
import scala.math.BigDecimal

object Planet extends Enumeration {
  type Planet = Value
  val Mercury, Venus, Earth_Moon_Barycenter, Mars, Jupiter, Saturn,
  Uranus, Neptune, Pluto, Moon_Geocentric, Sun, Nutations, Librations = Value
}


/**
 * The JPL Parser parses JPL planetary and lunar ephemerides [1] ASCII files [2].
 *
 * [1] ftp://ssd.jpl.nasa.gov/pub/eph/planets/README.txt
 * [2] ftp://ssd.jpl.nasa.gov/pub/eph/planets/ascii/ascii_format.txt
 *
 */
object JplParser {
  def parseDecimal(s: String): BigDecimal = BigDecimal(s.replace("D", "E"))


  /**
   * from [2]:
   * Word (1,i) is the starting location in each data record of the chebychev
coefficients belonging to the ith item.  Word (2,i) is the number of chebychev
coefficients per component of the ith item, and Word (3,i) is the number of
complete sets of coefficients in each data record for the ith item.
   */

  object Group extends Enumeration {
    val CONST_NAMES = 1040
    val CONST_VALUES = 1041
  }

  def parse(content: String): Map[String, BigDecimal] = {
    val rawGroups = content.split("""GROUP\s*""")

    parseConstantGroups(
      rawGroups.find(_ matches """^%d\n.*""".format(Group.CONST_NAMES)).getOrElse(throw new IllegalArgumentException(
        "The specified file does not include 'GROUP %d' for physical constant names".format(Group.CONST_NAMES)
      )),
      rawGroups.find(_ matches """^%d\n.*""".format(Group.CONST_VALUES)).getOrElse(throw new IllegalArgumentException(
        "The specified file does not include 'GROUP %d' for physical constant values".format(Group.CONST_NAMES)
      ))
    )



    ???
  }

  def parseConstantGroups(rawNames: String, rawValues: String): immutable.Map[String, BigDecimal] = {
    // split the strings by whitespace, then throw away empty items (unprecise split) and
    // the first two elements (Group ID and number of following items)
    def edit(s: String) = s.split( """(\s|\n)+""").filterNot(_.isEmpty).drop(2)

    // simply zip the lists into tuples (String, BigDecimal), and convert to Map
    edit(rawNames).zip(
      edit(rawValues).map(parseDecimal)
    ).toMap
  }

}
