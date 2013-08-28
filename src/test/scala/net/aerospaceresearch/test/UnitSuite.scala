package net.aerospaceresearch.test

import org.scalatest.FunSuite
import net.aerospaceresearch.units.{Seconds, Days}

/**
 * Created by elmar on 28.08.13.
 */
class UnitSuite extends FunSuite {

  test("Days") {
    val oneDay = Days(1)
    val oneSecond = Seconds(1)

    assert((oneDay + oneSecond).value === (1.0 / (24 * 60 * 60)) + 1)
  }

}
