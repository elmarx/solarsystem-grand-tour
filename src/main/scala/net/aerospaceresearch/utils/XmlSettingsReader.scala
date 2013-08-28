package net.aerospaceresearch.utils

import scala.xml.{Node, XML}
import net.aerospaceresearch.model.{Probe, Body}
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

  val bodies = (ssgt \ "bodies")(0).child.filter(_.label != "#PCDATA").map(xmlNodeToBody)

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
