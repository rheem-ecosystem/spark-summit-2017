lazy val root = (project in file(".")).settings(
  name := "spark-summit-demo",
  organization := "com.github.sekruse",
  version := "1.0-SNAPSHOT",
  scalaVersion := "2.11.8"
)

resolvers += Resolver.mavenLocal

libraryDependencies += "de.hpi.isg" % "profiledb-store" % "0.1.1"
libraryDependencies += "org.qcri.rheem" % "rheem-core" % "0.2.2-SNAPSHOT"
libraryDependencies += "org.jupyter-scala" % s"scala-api_${scalaVersion.value}" % "0.4.0-RC4"
libraryDependencies += "io.spray" %%  "spray-json" % "1.3.3"
