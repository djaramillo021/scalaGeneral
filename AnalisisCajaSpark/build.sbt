

name := "AnalisisCajaSpark"
 
version := "1.0.0"

scalaVersion := "2.11.5"



libraryDependencies ++= {
  val akkaVersion       = "2.3.7"
  val sprayVersion      = "1.3.1"
  Seq(
	    "org.apache.spark" %% "spark-core" % "1.3.0"

	)
}
 


