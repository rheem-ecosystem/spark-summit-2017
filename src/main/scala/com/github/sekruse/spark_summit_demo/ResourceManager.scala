package com.github.sekruse.spark_summit_demo

import java.io.{BufferedReader, InputStreamReader}

/**
  * Utility to manage files in the resource folder.
  */
object ResourceManager {

  private val cache = collection.mutable.Map[String, String]()

  /**
    * Provides the given resource and resolves variables.
    *
    * @param resource  the resource to provided
    * @param variables a dictionary of variable values
    * @return the provided resource
    */
  def get(resource: String, variables: Map[String, String] = null): String = {
    var loadedResource = this.cache.getOrElseUpdate(resource, load(resource))
    if (variables != null) {
      for ((identifier, value) <- variables) {
        loadedResource = loadedResource.replaceAllLiterally(s"$$$identifier", value)
      }
    }
    loadedResource
  }


  /**
    * Load the given resource.
    *
    * @param resource the resource to be loaded
    * @return the loaded resource
    */
  private def load(resource: String): String = {
    var reader: BufferedReader = null
    val sb = new StringBuilder
    try {
      reader = new BufferedReader(new InputStreamReader(this.getClass.getResourceAsStream(resource)))
      var line = reader.readLine()
      while (line != null) {
        sb.append(line).append('\n')
        line = reader.readLine()
      }
      sb.toString
    } finally {
      if (reader != null) reader.close()
    }
  }

}
