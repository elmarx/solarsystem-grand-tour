package net.aerospaceresearch.jplparser

import org.scalatest.FunSuite

/**
 * Created by elmar on 06.07.13.
 */
class PlanetSuite extends FunSuite {

  test("planet ids match item numbering in header") {

    assert(Planet.Earth_Moon_Barycenter.id === 2)
    assert(Planet.Mercury.id === 0)
  }

}
