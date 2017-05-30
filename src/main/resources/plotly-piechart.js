require(['plotly'], function(plotly) {
    // Inputs:
    var data = $data,
        divId = $divId;
    plotly.newPlot(divId, data);
});