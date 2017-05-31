package com.github.sekruse.spark_summit_demo

import java.util.concurrent.atomic.AtomicInteger

import com.github.sekruse.spark_summit_demo.json.JsonProtocol._
import jupyter.api.Publish
import spray.json._

import scala.collection.mutable

/**
  * This object provides entry points into Jupyter utilities.
  */
object JupyterAccess {

  private val idCounter = new AtomicInteger

  private var isInitialized = false

  private val requiredModules = mutable.Map(
    "plotly" -> "https://cdn.plot.ly/plotly-latest.min",
    "d3" -> "https://d3js.org/d3.v4.min"
  )

  def addModule(module: String, url: String)(implicit publish: Publish): Unit = {
    require(!isInitialized)
    requiredModules.put(module, url)
  }

  /**
    * Initializes the Jupyter utilities.
    */
  private[spark_summit_demo] def ensureInitialized()(implicit publish: Publish): Unit = {
    if (!isInitialized) {
      publish.js(ResourceManager.get("/initialize.js", Map("paths" -> requiredModules.toMap.toJson.toString)))
      isInitialized = true
    }
  }

  /**
    * Adds a new `div` element to the notebook with a new ID.
    *
    * @return the ID of the new element
    */
  private[spark_summit_demo] def addDiv(width: String = "100%",
                                        height: String = "500")
                                       (implicit publish: Publish): String = {
    val id = s"my-div-${idCounter.getAndAdd(1)}"
    publish.html(s"""<div id="$id" style="width: $width; height: $height"></div>""")
    id
  }

  /**
    * Adds a new `svg` element to the notebook with a new ID.
    *
    * @return the ID of the new element
    */
  private[spark_summit_demo] def addSvg(width: String = "100%",
                                        height: String = "100%")
                                       (implicit publish: Publish): String = {
    val id = s"my-svg-${idCounter.getAndAdd(1)}"
    publish.html(s"""<svg id="$id" style="width: $width; height: $height"></svg>""")
    id
  }

  private[spark_summit_demo] def nextId() = idCounter.getAndAdd(1)

}
