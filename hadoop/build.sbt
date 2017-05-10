import AssemblyKeys._

name := "hadoop-example"
organization := "com.virtuslab"
version := "0.0.1"
publishMavenStyle := true
crossPaths       := false
autoScalaLibrary := false

assemblySettings

val hadoopVersion = "2.7.1"
libraryDependencies ++= Seq(
  "org.apache.hadoop" % "hadoop-mapreduce-client-core" % hadoopVersion,
  "org.apache.hadoop" % "hadoop-common" % hadoopVersion
)

jarName in assembly := s"${name.value}-$hadoopVersion.jar" //Artifact name

mergeStrategy in assembly := {
  case PathList("META-INF", "MANIFEST.MF") => MergeStrategy.discard
  case _ => MergeStrategy.last
}

javacOptions ++= Seq("-source", "1.7", "-target", "1.7") //Same version as in Hadoop