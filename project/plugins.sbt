//
// Copyright (C) 2016 Lightbend Inc. <https://www.lightbend.com>
//

// The Lagom plugin
addSbtPlugin("com.lightbend.lagom" % "lagom-sbt-plugin" % "1.2.0")
// Needed for importing the project into Eclipse
//addSbtPlugin("com.typesafe.sbteclipse" % "sbteclipse-plugin" % "3.0.0")
// The ConductR plugin
addSbtPlugin("com.lightbend.conductr" % "sbt-conductr" % "2.1.16")
//plugin for packaging to docker image
addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.1.5")
//addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.2.0-SNAPSHOT")

