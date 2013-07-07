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

  object Group extends Enumeration {
    val CONST_NAMES = 1040
    val CONST_VALUES = 1041
    val TRIPLETS = 1050
    val DATA_RECORDS = 1070
  }

  /**
   * from [2]:
   * Word (1,i) is the starting location in each data record of the chebychev
   * coefficients belonging to the ith item.  Word (2,i) is the number of chebychev
   * coefficients per component of the ith item, and Word (3,i) is the number of
   * complete sets of coefficients in each data record for the ith item.
   */
  def parseTriplets(s: String): List[(Int, Int, Int)] = {
    val tripletStrings = s.split("""\n""").filterNot(_.trim.isEmpty).drop(1)
    val listOfTriplets = tripletStrings.map(_.split("""\s+"""))

    // decomposition the list of triplets
    val (startingLocations, numberOfChebychevCoefficients, numberOfCompleteSets) = listOfTriplets.toList match {
      case List(x, y, z) => (x, y, z)
      case _ => throw new IllegalArgumentException
    }

    // ok, now clean up the Lists, parse the ints, and pack together a list of Tuples to return
    (
      startingLocations.filterNot(_.isEmpty).map(_.toInt),
      numberOfChebychevCoefficients.filterNot(_.isEmpty).map(_.toInt),
      numberOfCompleteSets.filterNot(_.isEmpty).map(_.toInt)
    ).zipped.toList
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

    val triplets = parseTriplets(
      rawGroups.find(_ matches """^%d\n""".format(Group.TRIPLETS)).getOrElse(throw new IllegalArgumentException(
        "The specified file dos not include the triplet Group (GROUP %d)".format(Group.TRIPLETS)
      ))
    )




    ???
  }

  def parseConstantGroups(rawNames: String, rawValues: String): Map[String, BigDecimal] = {
    // split the strings by whitespace, then throw away empty items (unprecise split) and
    // the first two elements (Group ID and number of following items)
    def edit(s: String) = normalize(s).split(" ").drop(2)

    // simply zip the lists into tuples (String, BigDecimal), and convert to Map
    edit(rawNames).zip(
      edit(rawValues).map(parseDecimal)
    ).toMap
  }


  /**
   *
   * @param triplets Triplets as defined in group 1050
   * @return
   */
  def numberOfRecordsPerInterval(triplets: List[(Int, Int, Int)]): Int =
    // [2] states:
    /* There are three Cartesian components (x, y, z), for each of the items #1-11;
     * there are two components for the 12th item, nutations : d(psi) and d(epsilon);
     * there are three components for the 13th item, librations : three Euler angles.
     *
     * so, subtract the 12th item 1 time, (which is 0-based the 11th element)
     */
    triplets.map(x =>  (x._2 * 3) * x._3).sum - (triplets(11)._2 * triplets(11)._3)


  def normalize(s: String): String = s.replaceAll("""\n""", " ").replaceAll("""\s{2,}""", " ").trim
}
