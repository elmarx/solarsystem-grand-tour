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

package net.aerospaceresearch

import net.aerospaceresearch.jplparser.DataReader
import breeze.linalg.DenseVector
import org.joda.time.{DateTimeUtils, DateTime}
import net.aerospaceresearch.model.{SolarSystem, Body}
import net.aerospaceresearch.utils.{XmlSettingsReader, SolarSystemsCsvWriter}
import net.aerospaceresearch.units.Days


/**
 * TODO: add documentation
 *
 * User: Elmar Athmer
 * Part of: solarsystem-grand-tour
 */
object Main {

  def main(args: Array[String]) {

    // TODO: parse args for other input.xml files, so we can easily change these in batch runs
    val settings = new XmlSettingsReader("input.xml")

    val defaultSystem = new DataReader().system(settings.startTime)
    val systemWithMyBodies = SolarSystem(
      defaultSystem.bodies ++ settings.bodies,
      defaultSystem.centerMass,
      defaultSystem.time
    )

    val intermediateSystems = systemWithMyBodies.goto(
      Days(settings.startTime.value + settings.days),
      settings.recordResultsEvery, settings.leapSize
    )

    new SolarSystemsCsvWriter(intermediateSystems.reverse, settings.outputDir)
  }
}
