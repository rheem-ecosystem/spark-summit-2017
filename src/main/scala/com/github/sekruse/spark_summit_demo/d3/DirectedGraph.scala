package com.github.sekruse.spark_summit_demo.d3

import com.github.sekruse.spark_summit_demo.{JupyterAccess, ResourceManager}
import jupyter.api.Publish

import spray.json._
import com.github.sekruse.spark_summit_demo.json.JsonProtocol._


/**
  * Utility to plot directed graphs.
  * The d3 code is adapted from [[http://bl.ocks.org/mbostock/1153292]] and [[https://bl.ocks.org/mbostock/4062045]].
  */
object DirectedGraph {

  def plotDirectedGraph(nodes: Iterable[Node],
                        links: Iterable[Link],
                        width: String,
                        height: String)
                       (implicit publish: Publish) = {

    // Initialize the canvas.
    val divId = JupyterAccess.addDiv(height = height)

    // Build the JS script.
    val js = ResourceManager.get("/d3-directed-graph.js", Map(
      "divId" -> divId.toJson.toString,
      "nodes" -> nodes.toJson.toString,
      "links" -> links.toJson.toString
    ))
    publish.js(js)
  }

  case class Node(id: Int, name: String, label: String, radius: Int, color: String)

  case class Link(source: Int, target: Int)

}
