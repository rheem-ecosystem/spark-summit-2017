package com.github.sekruse.spark_summit_demo.plotly


/**
  * Describes the plot.ly's trace objects.
  */
case class CategoryTrace(`type`: String,
                         values: Seq[Double],
                         labels: Seq[String],
                         name: Option[String],
                         hoverinfo: Option[String],
                         showlegend: Boolean)