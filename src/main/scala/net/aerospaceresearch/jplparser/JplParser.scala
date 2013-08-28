package net.aerospaceresearch.jplparser

import Types._
import net.aerospaceresearch.units.Days




/**
 * The JPL Parser parses JPL planetary and lunar ephemerides [1] ASCII files [2].
 *
 * [1] ftp://ssd.jpl.nasa.gov/pub/eph/planets/README.txt
 * [2] ftp://ssd.jpl.nasa.gov/pub/eph/planets/ascii/ascii_format.txt
 *
 */
object JplParser {
  def parseDecimal(s: String): BigDecimal =
    try {
      BigDecimal(s.replace("D", "E"))
    }
    catch {
      case e: NumberFormatException => throw new IllegalArgumentException("Cannot parse %s".format(s), e)
    }

  object Group {
    val CONST_NAMES = 1040
    val CONST_VALUES = 1041
    val TRIPLETS = 1050
    val DATA_RECORDS = 1070
    val TIMING_DATA = 1030
  }

  /**
   * from [2]:
   * Word (1,i) is the starting location in each data record of the chebychev
   * coefficients belonging to the ith item.  Word (2,i) is the number of chebychev
   * coefficients per component of the ith item, and Word (3,i) is the number of
   * complete sets of coefficients in each data record for the ith item.
   */
  def parseTriplets(s: String): List[(Int, Int, Int)] = {
    val list = normalize(s).split(" ").drop(1).map(_.toInt)

    (
      list.slice(0, 13),
      list.slice(13, 26),
      list.slice(26, 39)
      ).zipped.toList
  }

  /**
   * parse the triplets and augment them with the number of components per coefficient (for nutations,
   * there are only two components, for all other entities, there are three)
   * @param s the row group, as written in the header
   * @return
   */
  def parseQuartets(s: String): List[(Int, Int, Int, Int)] = {
    parseTriplets(s).zipWithIndex.map {
      case ((a, b, c), EntityAssignments.Nutations) => (a, b, c, 2)
      case ((a, b, c), _) => (a, b, c, 3)
    }
  }

  /**
   * parses the constants defined in header.yyy
   *
   * @param rawNames the complete group of constant names
   * @param rawValues the complete group of values
   * @return
   */
  def parseConstantGroups(rawNames: String, rawValues: String): Map[String, BigDecimal] = {
    // split the strings by whitespace, and drop
    // the first two elements (Group ID and number of following items)
    def edit(s: String) = normalize(s).split(" ").drop(2)

    // simply zip the lists into tuples (String, BigDecimal), and convert to Map
    edit(rawNames).zip(
      edit(rawValues).map(parseDecimal)
    ).toMap
  }


  /**
   *
   * @param quartets Triplets as defined in group 1050, augmented by number of components
   * @return
   */
  def recordsPerInterval(quartets: List[(Int, Int, Int, Int)]): Int =
  // [2] states:
  /* There are three Cartesian components (x, y, z), for each of the items #1-11;
   * there are two components for the 12th item, nutations : d(psi) and d(epsilon);
   * there are three components for the 13th item, librations : three Euler angles.
   */
    quartets.map {
      case (_, coefficients, completeSets, components) => coefficients * components * completeSets
    }.sum


  /**
   * normalizes a string, i.e. replace all linebreaks to normal spaces, and remove duplicate spaces
   * @param s an arbitrary string to normalize
   * @return
   */
  def normalize(s: String): String = s.replaceAll( """\n""", " ").replaceAll( """\s{2,}""", " ").trim

  /**
   * parses the timing data (start and end date, interval size)
   * @param s the complete group 1030 from header.yyy
   * @return
   */
  def parseTimingData(s: String): (Days, Days, Double) = {
    val list = normalize(s).split(" ").drop(1).map(_.toDouble)

    (Days(list(0)), Days(list(1)), list(2))
  }


  def parseDataRecords(s: String, quartets: List[(Int, Int, Int, Int)]): List[AstronomicalObject] = {
    val numberOfRecordsPerInterval = recordsPerInterval(quartets)
    val rawList = normalize(s).split(" ").map(parseDecimal).toList

    for ((entity, index) <- quartets.zipWithIndex)
      yield AstronomicalObject(
        index,
        extractIntervalsForEntity(entity, groupOfIntervals(rawList, numberOfRecordsPerInterval))
      )
  }

  /**
   * group the raw List of records by intervals
   *
   * each interval contains: the index, the number of Records, the start date, the end date (take these!)
   * than a list of Records (take these!), and the trailing entries (ignore them)
   *
   * @param rawList flat list of items of type T
   * @param recordsPerInterval how many records are being stored per interval
   * @tparam T
   * @return
   */
  def groupOfIntervals[T](rawList: List[T], recordsPerInterval: Int): List[List[T]] = {
    // intervalLength is the total number of records per interval
    val intervalLength: Int = 2 + 2 + recordsPerInterval + numberOfTrailingEntries(recordsPerInterval)

    for (interval <- rawList.grouped(intervalLength).toList)
    yield interval.drop(2).take(2 + recordsPerInterval)
  }


  /**
   * extracts from a grouped list of flat records the Intervals
   *
   * @param intervals flat list of records, grouped by entityId
   * @return Intervals with start and end, plus the corresponding sets of coefficients
   */
  def extractIntervalsForEntity(quartet: (Int, Int, Int, Int), intervals: List[List[BigDecimal]]): List[Interval] = {
    val (firstRecord, xCoefficients, xCompleteSets, xComponents) = quartet

    intervals.map {
      case startingTime :: endingTime :: records => {

        val myRecords: List[List[BigDecimal]] = records.drop(firstRecord - 3).
          take(xCoefficients * xComponents * xCompleteSets).grouped(xCoefficients * xComponents).toList

        Interval(
          Days(startingTime.toDouble),
          Days(endingTime.toDouble),
          myRecords.map(CoefficientSet(_, xCoefficients))
        )
      }
      case _ => throw new IllegalArgumentException("the data do not contain correct intervals")
    }
  }


  /**
   * get the number of trailing entries in an interval
   *
   * There are always three entries per row, so if the the last row could be filled with "zero" entries if the
   * number of records is not dividable by 3
   *
   * @param records number of records per interval (as given in the header)
   * @return
   */
  def numberOfTrailingEntries(records: Int) = 3 - ((records + 2) % 3) // add 2 for the two prefixed julian date entries


  /**
   * This function puts together the pieces of parsing, and generates a service based on raw file input
   * @param header the complete header.yyy as string
   * @param data the data, at least complete intervals taken from ascpXXXX.yyy files
   */
  def generateService(header: String, data: String) = {
    def findGroup(id: Int)(s: String): Boolean =
      """%d\n""".format(id).r.findFirstIn(s).isDefined

    val rawGroups = header.split( """GROUP\s*""")

    val quartets = parseQuartets(
        rawGroups.find(findGroup(Group.TRIPLETS)).getOrElse(
         throw new IllegalArgumentException(
           "The specified file does not include 'GROUP %d' for physical constant values".format(Group.CONST_NAMES)
        ))
    )

    val timingData = parseTimingData(
      rawGroups.find(findGroup(Group.TIMING_DATA)).getOrElse(throw new
          IllegalArgumentException(
        "The specified file does not include the timing information Group (GROUP %d)".format(Group.TIMING_DATA)
      ))
    )

    val constants = parseConstantGroups(
      rawGroups.find(findGroup(Group.CONST_NAMES)).getOrElse(throw new IllegalArgumentException(
        "The specified file does not include 'GROUP %d' for physical constant names".format(Group.CONST_NAMES)
      )),
      rawGroups.find(findGroup(Group.CONST_VALUES)).getOrElse(throw new IllegalArgumentException(
        "The specified file does not include 'GROUP %d' for physical constant values".format(Group.CONST_VALUES)
      ))
    )

    val objects = parseDataRecords(data, quartets)

    new EphemerisService(quartets, objects, timingData, constants)
  }
}
