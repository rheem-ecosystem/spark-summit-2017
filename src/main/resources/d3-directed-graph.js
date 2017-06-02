requirejs(['d3'], function (d3) {
    "use strict";

    // ID of the DIV to render in.
    var divId = $divId;
    var data = {
        // Graph nodes. Format: [{id: <ID>, name: <name>, label: <type>, radius: <radius>, color: <color>}, ...]
        nodes: $nodes,
        // Graph links. Format: [{source: <source node ID>, target: <destination node ID>}, ...]
        links: $links,
        // Graph nodes.
        legend: $legend
    };

    // Obtain the DIV we are supposed to work in.
    var div = document.getElementById(divId);
    div.style.position = "relative";
    var width = div.clientWidth, height = div.clientHeight;
    var margin = {top: 0, left: 0, bottom: 0, right: 0};
    var chartWidth = width - margin.left - margin.right, chartHeight = height - margin.top - margin.bottom;

    // Add an SVG to draw the chart in.
    var svg = d3.select("#" + divId).append("svg");
    svg.attr("width", width).attr("height", height);

    // Add a chart layer.
    var chartLayer = svg.append("g").classed("chartLayer", true);
    chartLayer
        .attr("width", chartWidth)
        .attr("height", chartHeight)
        .attr("transform", "translate(" + [margin.left, margin.top] + ")");

    // Create the tooltip.
    var tooltip = d3.select("#" + divId).append("div")
        .style("position", "absolute")
        .style("padding", "5px")
        .style("border", "1px")
        .style("border-style", "black")
        .style("border-color", "green")
        .style("border-radius", "4px")
        .style("background", "#CCCCCC")
        .style("pointer-events", "none")
        .style("font", "12px sans-serif")
        .style("color", "white")
        .style("text-shadow", "0px 0px 3px black")
        .style("opacity", 0);

    // Create the force simulation.
    var simulation = d3.forceSimulation()
        .force("link", d3.forceLink().id(function (d) { return d.id }).distance(35).strength(2.8))
        .force("collide", d3.forceCollide(function (d) { return d.radius + 8 }).iterations(16))
        .force("charge", d3.forceManyBody().strength(-100))
        .force("center", d3.forceCenter(chartWidth / 2, chartHeight / 2))
        .force("y", d3.forceY(0))
        .force("x", d3.forceX(0));

    // Render the links.
    svg.append("defs").selectAll("marker")
        .data(["default"])
        .enter().append("marker")
        .attr("id", function(d) { return d; })
        .attr("viewBox", "0 0 10 10")
        .attr("refX", 10)
        .attr("refY", 5)
        .attr("markerWidth", 10)
        .attr("markerHeight", 10)
        .attr("orient", "auto")
        .attr("fill", "black")
        .append("path")
        .attr("d", "M 0 0 L 10 5 L 0 10 Z");

    var link = svg.append("g")
        .attr("class", "links")
        .selectAll("line")
        .data(data.links)
        .enter()
        .append("line")
        .attr("stroke", "black")
        .attr("marker-end", "url(#default)");

    // Render the nodes.
    var node = svg.append("g")
        .attr("class", "nodes")
        .selectAll("circle")
        .data(data.nodes)
        .enter().append("circle")
        .attr("r", function (d) { return d.radius })
        .attr("fill", function (d) { return d.color })
        .attr("stroke", "black")
        .attr("opacity", 0.9)
        .on("mouseover", function(d) {
            tooltip.transition()
                .duration(200)
                .style("opacity", 1);
            tooltip.html("<b>" + d.name + "</b><br/>" + d.label)
                .style("background", d.color)
                .style("left", (d3.mouse(div)[0] + 20) + "px")
                .style("top", (d3.mouse(div)[1] + 20) + "px");
        })
        .on("mousemove", function(d) {
            tooltip.html("<b>" + d.name + "</b><br/>" + d.label)
                .style("background", d.color)
                .style("left", (d3.mouse(div)[0] + 20) + "px")
                .style("top", (d3.mouse(div)[1] + 20) + "px");
        })
        .on("mouseout", function(d) {
            tooltip.transition()
                .duration(500)
                .style("opacity", 0);
        })
        .call(d3.drag()
            .on("start", dragstarted)
            .on("drag", dragged)
            .on("end", dragended));

    function shiftOnLine(source, target, point, width) {
        var dx = target.x - source.x,
            dy = target.y - source.y;
        var l = Math.sqrt(dx * dx + dy * dy);
        if (l == 0) return point;
        return {
            x: point.x + width * dx / l,
            y: point.y + width * dy / l
        };
    }


    // Render the legend.
    svg.append("g")
        .selectAll("circle")
        .data(data.legend)
        .enter().append("circle")
        .attr("r", function (d) { return d.radius })
        .attr("fill", function (d) { return d.color })
        .attr("transform", function (d, i) { return "translate(15," + (30 * i + 15) + ")" })
        .attr("stroke", "black")
        .attr("opacity", 0.9);
    svg.append("g")
        .selectAll("text")
        .data(data.legend)
        .enter().append("text")
        .attr("x", function(d, i) { return 35 })
        .attr("y", function(d, i) { return 30 * i + 20; })
        .text( function (d) { return d.name; })
        .attr("font-family", "sans-serif")
        .attr("font-size", "14px")
        .attr("fill", "black");

    // Register the nodes of with the force simulation.
    var ticked = function () {
        link
            .attr("x1", function (d) { return shiftOnLine(d.source, d.target, d.source, d.source.radius).x; })
            .attr("y1", function (d) { return shiftOnLine(d.source, d.target, d.source, d.source.radius).y; })
            .attr("x2", function (d) { return shiftOnLine(d.source, d.target, d.target, -d.target.radius).x; })
            .attr("y2", function (d) { return shiftOnLine(d.source, d.target, d.target, -d.target.radius).y; });

        node
            .attr("cx", function (d) { return d.x; })
            .attr("cy", function (d) { return d.y; });
    };

    simulation
        .nodes(data.nodes)
        .on("tick", ticked);

    // Register the links with the force simulation.
    simulation.force("link")
        .links(data.links);


    function dragstarted(d) {
        if (!d3.event.active) simulation.alphaTarget(0.3).restart();
        d.fx = d.x;
        d.fy = d.y;
    }

    function dragged(d) {
        d.fx = d3.event.x;
        d.fy = d3.event.y;
    }

    function dragended(d) {
        if (!d3.event.active) simulation.alphaTarget(0);
        d.fx = null;
        d.fy = null;
    }
});