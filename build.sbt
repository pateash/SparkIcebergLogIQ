// give the user a nice default project!

val sparkVersion = settingKey[String]("Spark version")

lazy val root = (project in file("."))
  .settings(
    inThisBuild(List(
      organization := "in.pateash",
      scalaVersion := "2.12.19"
    )),
    name := "SparkIcebergLogIQ",
    version := "0.0.1",

    sparkVersion := "3.5.1",

    javacOptions ++= Seq("-source", "1.8", "-target", "1.8"),
    javaOptions ++= Seq("-Xms512M", "-Xmx2048M"),
    scalacOptions ++= Seq("-deprecation", "-unchecked"),
    Test / parallelExecution := false,
    fork := true,

    coverageHighlighting := true,

    libraryDependencies ++= Seq(
      "org.apache.spark" %% "spark-streaming" % sparkVersion.value,
      "org.apache.spark" %% "spark-sql" % sparkVersion.value,
      //      "org.apache.iceberg" %% "iceberg-spark-runtime-3.5" % "1.7.0",
      //      "software.amazon.awssdk" % "aws-java-sdk-bundle" % "1.8",
      //      "software.amazon.awssdk" % "bundle" % "2.29.14",
      "com.amazonaws" % "aws-java-sdk-bundle" % "1.12.520", // Add AWS SDK
      "org.apache.hadoop" % "hadoop-common" % "3.3.4",
      "org.apache.hadoop" % "hadoop-client" % "3.3.4",
      "org.apache.hadoop" % "hadoop-aws" % "3.3.4", // Use your Hadoop version here

      // TESTING
      //      "org.scalacheck" %% "scalacheck" % "1.15.4" % "test",
      //      "org.scalatest" %% "scalatest" % "3.2.19" % "test",
      //      "com.holdenkarau" %% "spark-testing-base" % s"${sparkVersion.value}_1.4.3"
      //      % "test" // not supported for spark 3.5.1 yet
      "org.apache.iceberg" %% "iceberg-spark-runtime-3.5" % "1.5.0",
      //      "org.apache.iceberg" %% "iceberg-spark-runtime-3.5" % "1.5.0" exclude("org.apache.hadoop", "hadoop-client"),

      // LOGGING and monitoring
      "com.datadoghq" % "java-dogstatsd-client" % "4.0.0"
    ),

    // uses compile classpath for the run task, including "provided" jar (cf http://stackoverflow.com/a/21803413/3827)
    Compile / run := Defaults.runTask(Compile / fullClasspath, (Compile / run) / mainClass, (Compile / run) / runner).evaluated,

    scalacOptions ++= Seq("-deprecation", "-unchecked"),
    pomIncludeRepository := { _ => false },

    resolvers ++= Seq(
      "sonatype-releases" at "https://oss.sonatype.org/content/repositories/releases/",
      "Typesafe repository" at "https://repo.typesafe.com/typesafe/releases/",
      "Second Typesafe repo" at "https://repo.typesafe.com/typesafe/maven-releases/",
      "sonatype-public" at "https://oss.sonatype.org/content/repositories/public/"
    ),

    pomIncludeRepository := { _ => false },

    // publish settings
    publishTo := {
      val nexus = "https://oss.sonatype.org/"
      if (isSnapshot.value)
        Some("snapshots" at nexus + "content/repositories/snapshots")
      else
        Some("releases" at nexus + "service/local/staging/deploy/maven2")
    }
  )
