/*
 * Copyright (c) 2013 Elmar Athmer
 *
 * This file is part of SolarSystemGrandTour.
 *
 * SolarSystemGrandTour is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * SolarSystemGrandTour is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with SolarSystemGrandTour.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.aerospaceresearch.test.jplparser

import org.scalatest.FunSuite
import net.aerospaceresearch.jplparser.EntityAssignments

class EntitySuite extends FunSuite {

  test("astronomical object ids match item numbering in header") {

    assert(EntityAssignments.AstronomicalObjects.Earth_Moon_Barycenter.id === 2)
    assert(EntityAssignments.AstronomicalObjects.Mercury.id === 0)
  }

  test("11 'planets' at the moment") {
    assert(EntityAssignments.AstronomicalObjects.maxId === 11)
  }

  test("nutation and liberation") {
    assert(EntityAssignments.Librations === 12)
    assert(EntityAssignments.Nutations === 11)
  }

}
