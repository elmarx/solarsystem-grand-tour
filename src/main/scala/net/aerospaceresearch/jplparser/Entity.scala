package net.aerospaceresearch.jplparser

/**
 * Created by elmar on 11.07.13.
 */
class Entity(val id: Int,
           startingLocation: Int,
           numberOfCoefficients: Int,
           numberOfCompleteSets: Int,
           completeRecords: List[BigDecimal],
           recordsPerInterval: Int
) {
  val numberOfPolynoms = if(id == 11) 2 else 3
  private val startingIndex = startingLocation - 3

  /**
   * List of Records specific for this entity
   */
  lazy val records: List[BigDecimal] = {
    // how many items of an interval belong to this Entity
    val myRecordsPerInterval = numberOfCoefficients * numberOfPolynoms * numberOfCompleteSets

    def recordsIter(completeRecords: List[BigDecimal], collectedRecords: List[BigDecimal]): List[BigDecimal] = {
      if(completeRecords.isEmpty) collectedRecords
      else recordsIter(
        completeRecords.drop(recordsPerInterval),
        collectedRecords ::: completeRecords.drop(startingIndex).take(myRecordsPerInterval)
      )
    }

    recordsIter(completeRecords, Nil)
  }
}
