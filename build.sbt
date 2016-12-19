import com.typesafe.sbt.SbtNativePackager.packageArchetype

name := "publisher"
version := "0"

organization in ThisBuild := "sample.helloworld"

// the Scala version that will be used for cross-compiled libraries
scalaVersion in ThisBuild := "2.11.8"

//something suggested for faster refreshes
updateOptions in Global := updateOptions.in(Global).value.withCachedResolution(true)

// adds start script and jar mappings
packageArchetype.java_application

// the docker maintainer. You could scope this to "in Docker"
maintainer := "Author"

// Short package description
packageSummary := "Publisher"

//lazy val publisher = project("publisher")
//  .enablePlugins(DockerPlugin)

lazy val helloworldApi = project("helloworld-api")
  .settings(
    version := "1.0-SNAPSHOT",
    libraryDependencies += lagomJavadslApi
  )
  .settings(dockerRepository := Some("michal334/workspace"))

lazy val helloworldImpl = project("helloworld-impl")
  .enablePlugins(LagomJava)
  .settings(
    version := "1.0-SNAPSHOT",
    libraryDependencies ++= Seq(
      //Dependency for Kafka needed for publisher
      lagomJavadslKafkaBroker,
      lagomJavadslPersistenceCassandra,
      lagomJavadslTestKit
    )
  )
  .settings(lagomForkedTestSettings: _*)
  .settings(dockerRepository := Some("michal334/workspace"))
  .dependsOn(helloworldApi)

lazy val subscriberApi = project("subscriber-api")
  .settings(
    version := "1.0-SNAPSHOT",
    libraryDependencies += lagomJavadslApi
  )
  .settings(dockerRepository := Some("michal334/workspace"))

lazy val subscriberImpl = project("subscriber-impl")
  .enablePlugins(LagomJava)
  .settings(
    version := "1.0-SNAPSHOT",
    libraryDependencies ++= Seq(
      //Dependency for Kafka - subcriber only
      lagomJavadslKafkaClient
    )
  )
  .settings(lagomForkedTestSettings: _*)
  .settings(dockerRepository := Some("michal334/workspace"))
  .dependsOn(subscriberApi, helloworldApi)
//  .dependsOn(helloworldApi)
//  .dependsOn(helloworldImpl)


def project(id: String) = Project(id, base = file(id))
  .settings(eclipseSettings: _*)
  .settings(javacOptions in compile ++= Seq("-encoding", "UTF-8", "-source", "1.8", "-target", "1.8", "-Xlint:unchecked", "-Xlint:deprecation"))
  .settings(jacksonParameterNamesJavacSettings: _*) // applying it to every project even if not strictly needed.
  .settings(
  publishArtifact in (Compile, packageDoc) := false,
  publishArtifact in packageDoc := false,
  sources in (Compile,doc) := Seq.empty
)
  .settings(dockerRepository := Some("michal334/workspace"))


// See https://github.com/FasterXML/jackson-module-parameter-names
lazy val jacksonParameterNamesJavacSettings = Seq(
  javacOptions in compile += "-parameters"
)

// Configuration of sbteclipse
// Needed for importing the project into Eclipse
lazy val eclipseSettings = Seq(
  EclipseKeys.projectFlavor := EclipseProjectFlavor.Java,
  EclipseKeys.withBundledScalaContainers := false,
  EclipseKeys.createSrc := EclipseCreateSrc.Default + EclipseCreateSrc.Resource,
  EclipseKeys.eclipseOutput := Some(".target"),
  EclipseKeys.withSource := true,
  EclipseKeys.withJavadoc := true,
  // avoid some scala specific source directories
  unmanagedSourceDirectories in Compile := Seq((javaSource in Compile).value),
  unmanagedSourceDirectories in Test := Seq((javaSource in Test).value)
)
