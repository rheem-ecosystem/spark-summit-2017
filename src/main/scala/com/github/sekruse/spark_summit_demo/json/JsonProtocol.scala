package com.github.sekruse.spark_summit_demo.json

import com.github.sekruse.spark_summit_demo.plotly.CategoryTrace
import spray.json.{DefaultJsonProtocol, JsonFormat}

/**
  * Enables usage of Spray JSON with custom classes in this project.
  */
object JsonProtocol extends DefaultJsonProtocol {
  implicit def categoryTraceFormat: JsonFormat[CategoryTrace] = jsonFormat6(CategoryTrace)
}
