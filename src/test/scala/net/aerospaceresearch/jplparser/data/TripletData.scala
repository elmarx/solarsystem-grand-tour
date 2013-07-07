package net.aerospaceresearch.jplparser.data

/**
 * test data taken from ftp://ssd.jpl.nasa.gov/pub/eph/planets/ascii/de423/header.423
 */
trait TripletData {
  val tripletGroup =
    """
      |   1050
      |
      |     3   171   231   309   342   366   387   405   423   441   753   819   899
      |    14    10    13    11     8     7     6     6     6    13    11    10    10
      |     4     2     2     1     1     1     1     1     1     8     2     4     4
    """.stripMargin
}
