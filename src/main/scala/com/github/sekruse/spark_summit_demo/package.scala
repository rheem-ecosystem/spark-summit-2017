package com.github.sekruse

import _root_.jupyter.api.Publish
import com.github.sekruse.spark_summit_demo.d3.DirectedGraph
import com.github.sekruse.spark_summit_demo.plotly.PieChart
import de.hpi.isg.profiledb.store.model.Experiment
import org.qcri.rheem.core.profiling.ExecutionPlanMeasurement

import scala.collection.JavaConversions._


/**
  * Provides utility functions for Jupyter.
  */
package object spark_summit_demo {

  val platformColors = scala.collection.mutable.Map(
    "Java Streams" -> "#0099CC",
    "Apache Spark" -> "#FF6600",
    "Channel" -> "#999999"
  )

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

  def plotExecutionPlan(experiment: Experiment, executionPlanId: String = "execution-plan-0")(implicit publish: Publish) = {
    experiment.getMeasurements.find(_.getId == executionPlanId) match {
      case Some(plan: ExecutionPlanMeasurement) =>
        val nodes = plan.getChannels.map(channel => DirectedGraph.Node(
          id = channel.getId,
          name = channel.getType,
          label = s"Data quanta type: ${channel.getDataQuantaType}",
//          label = s"Data quanta type: ${channel.getDataQuantaType.split("\\.").last}",
          radius = 10,
          color = platformColors("Channel")
        )) ++ plan.getOperators.map(op => DirectedGraph.Node(
          id = op.getId,
          name = if (op.getName ne null) op.getName else "(no name)",
          label = op.getType.split("\\.").last,
          radius = 10,
          color = platformColors(op.getPlatform)
        ))
        val links = plan.getLinks.map(link => DirectedGraph.Link(link.getSource, link.getDestination))
        DirectedGraph.plotDirectedGraph(
          nodes, links, width = "100%", height = "500px"
        )
      case _ => sys.error("Could not find an execution plan.")
    }
  }

  def addModule(module: String, url: String)(implicit publish: Publish): Unit = JupyterAccess.addModule(module, url)

}
