/*
 * Copyright (c) 2014 Elmar Athmer
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

package net.aerospaceresearch.utils

import scala.xml.{Node, XML}
import net.aerospaceresearch.model.{RelativeBody, Probe, Body}
import breeze.linalg.DenseVector
import net.aerospaceresearch.units.{Seconds, Days}

/**
 *
 * Created by elmar on 18.08.13.
 */
class XmlSettingsReader(file: String) {

  val ssgt = XML.loadFile(file)
  val settings = ssgt \ "settings"

  val startTime = Days((settings \ "startTime").text.toDouble)
  val days = (settings \ "days" ).text.toInt
  val outputDir = (settings \ "outputDir" ).text
  val recordResultsEvery = Days((settings \ "recordResultsEvery").text.toDouble)
  val leapSize = Seconds((settings \ "leapSize").text.toDouble)

  val bodies = (ssgt \ "bodies" \ "_").filter(_.attribute("system").isEmpty).map(xmlNodeToBody)
  val relativeBodies = (ssgt \ "bodies" \ "_").filter(_.attribute("system").isDefined).map(
    n => new RelativeBody(xmlNodeToBody(n), (n \ "@system").text)
  )

  def xmlNodeToBody(e: Node): Body = {
    val name = (e \ "name").text
    val mass = (e \ "mass").text.toDouble
    val r = DenseVector[Double](
      (e \ "r" \ "@x").text.toDouble,
      (e \ "r" \ "@y").text.toDouble,
      (e \ "r" \ "@z").text.toDouble
    )
    val v = DenseVector[Double](
      (e \ "v" \ "@x").text.toDouble,
      (e \ "v" \ "@y").text.toDouble,
      (e \ "v" \ "@z").text.toDouble
    )

    e.label match {
      case "probe" => Probe(name, mass, r, v)
      case x => throw new Exception("Don't know how to handle node of type " + x)
    }
  }

}
