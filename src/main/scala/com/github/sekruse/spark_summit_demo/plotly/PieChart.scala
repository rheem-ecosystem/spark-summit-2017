package com.github.sekruse.spark_summit_demo.plotly

import com.github.sekruse.spark_summit_demo.{JupyterAccess, ResourceManager}
import jupyter.api.Publish
import spray.json._
import com.github.sekruse.spark_summit_demo.json.JsonProtocol._

/**
  * This class creates pie charts using plot.ly.
  */
private[spark_summit_demo] object PieChart {

  def plot[T](data: Iterable[T],
              values: T => Double,
              labels: T => String,
              name: String = null,
              hoverinfo: String,
              showlegend: Boolean)
             (implicit publish: Publish)
  : Unit = {

    // Create a div.
    val divId = JupyterAccess.addDiv()

    // Create the plot.ly trace.
    val orderedData = data.toSeq
    val jsonData = List(CategoryTrace(
      `type` = "pie",
      values = orderedData.map(values),
      labels = orderedData.map(labels),
      name = Option(name),
      hoverinfo = Option(hoverinfo),
      showlegend = showlegend
    )).toJson
    val jsonDivId = divId.toJson

    val js = ResourceManager.get("/plotly-piechart.js", Map(
      "divId" -> jsonDivId.toString,
      "data" -> jsonData.toString)
    )
    publish.js(js)
  }

}
