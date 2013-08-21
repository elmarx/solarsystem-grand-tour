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

package net.aerospaceresearch.test

import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter
import net.aerospaceresearch.model.SolarSystem
import net.aerospaceresearch.jplparser.DataReader

/**
 * Created by elmar on 21.08.13.
 */
class SolarSystemSuite extends FunSuite with BeforeAndAfter {

  test("several step") {
    val startTime = 2456520
    val initialSystem = new DataReader().system(2456520)

    val nextSystem = initialSystem.goto(startTime + 1)(0)




  }

}
