package com.github.sekruse

import _root_.jupyter.api.Publish
import com.github.sekruse.spark_summit_demo.plotly.PieChart


/**
  * Provides utility functions for Jupyter.
  */
package object spark_summit_demo {

  def plotPieChart[T](data: Iterable[T],
                      values: T => Double,
                      labels: T => String,
                      name: String = null,
                      hoverinfo: String = "label+value+percent",
                      showlegend: Boolean = true)
                     (implicit publish: Publish)
  : Unit = {
    JupyterAccess.ensureInitialized()
    PieChart.plot(data, values, labels, name, hoverinfo, showlegend)
  }

  def requireJs(module: String, url: String)(implicit publish: Publish): Unit = JupyterAccess.requireJs(module, url)

}
