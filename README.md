# Rheem Demo for the Spark Summit 2017 <img align="right" width="128px" src="http://da.qcri.org/rheem/images/rheem.png" alt="Rheem logo">

This repository contains [Jupyter](https://jupyter.org/) notebooks that demonstrate how to program and run Rheem applications. In particular, we provide a notebook with the good ol' WordCount and a notebook running k-means on data that is stored in a SQLite database.

## Running instructions

Above mentioned notebooks can be found in the `notebooks/` folder. To run them, you will need to perform the following steps:
1. Install [Jupyter](https://jupyter.org/), which is necessary to serve the notebooks via a browser.
2. The notebooks are written in Scala. Specifically, you will need the [Jupyter Scala kernel](https://jupyter-scala.org) to execute the code in the notebooks.
3. The notebooks depend on some code in the `src/` folder for the fancy visualizations... :sunglasses: To use this code, you will need to have [SBT](http://www.scala-sbt.org/) installed. Then simply run from the root folder of this repository
```
$ sbt publish-local
```
4. You are all set. Move to the `notebooks/` folder and start Jupyter:
```
$ cd notebooks
$ jupyter-notebook
```
5. You can now interact with Jupyter via the browser, typically at [localhost:8888](http://localhost:8888). The `*.ipynb` files contain the notebooks. There is no need to manually prepare any datasets as these will be generated or downloaded directly in the notebooks.
