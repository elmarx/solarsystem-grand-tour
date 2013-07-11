package net.aerospaceresearch.jplparser

/**
 * Created by elmar on 11.07.13.
 */
class Entity(val id: Int,
           val startingLocation: Int,
           val numberOfCoefficients: Int,
           val numberOfCompleteSets: Int
) {
  val numberOfPolynoms = if(id == 11) 2 else 3

}
