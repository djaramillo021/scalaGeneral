name := "tentarseplayscala"

version := "1.0.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)


// scalaVersion := "2.11.5"

scalaVersion := "2.11.5"


// scalacOptions ++= Seq("-Xmax-classfile-name", "220")
// "org.mongodb" %% "casbah" % "2.7.2"


resolvers ++= Seq("Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/",
                  "Sonatype snapshots"  at "http://oss.sonatype.org/content/repositories/snapshots/",
                  "Spray Repository"    at "http://repo.spray.io",
                  "Spray Nightlies"     at "http://nightlies.spray.io/")


libraryDependencies ++= Seq(
  filters,
  jdbc,
  anorm,
  cache,
  ws,
  //"se.digiplant" %% "play-scalr" % "1.1.2",
  "org.mongodb" %% "casbah" % "2.7.2",
  "com.novus" %% "salat" % "1.9.9",
  "com.squareup.okhttp" % "okhttp-urlconnection" % "2.0.0",
  "com.google.apis" % "google-api-services-storage" % "v1-rev4-1.18.0-rc",
  "commons-codec" % "commons-codec" % "1.9",
  "com.google.http-client" % "google-http-client-jackson2" % "1.17.0-rc",
  "com.googlecode.json-simple" % "json-simple" % "1.1.1",
  "com.google.guava" % "guava" % "17.0",
  "se.radley" %% "play-plugins-salat" % "1.5.0",
  "com.sksamuel.scrimage" %% "scrimage-core" % "1.4.2",
  "com.sksamuel.scrimage" %% "scrimage-canvas" % "1.4.2",
  "com.sksamuel.scrimage" %% "scrimage-filters" % "1.4.2"
)

