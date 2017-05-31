package com.github.sekruse.spark_summit_demo.json

import com.github.sekruse.spark_summit_demo.d3.DirectedGraph.{Link, Node}
import com.github.sekruse.spark_summit_demo.plotly.CategoryTrace
import spray.json.{DefaultJsonProtocol, JsonFormat}

/**
  * Enables usage of Spray JSON with custom classes in this project.
  */
object JsonProtocol extends DefaultJsonProtocol {

  implicit def categoryTraceFormat: JsonFormat[CategoryTrace] = jsonFormat6(CategoryTrace)

  implicit def directedGraphNodeFormat: JsonFormat[Node] = jsonFormat5(Node)

  implicit def directedGraphLinkFormat: JsonFormat[Link] = jsonFormat2(Link)

}
