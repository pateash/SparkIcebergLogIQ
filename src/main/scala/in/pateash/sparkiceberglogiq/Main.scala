package in.pateash.sparkiceberglogiq

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._
import org.apache.hadoop.fs.s3a.S3AFileSystem

object Main extends InitSpark {
  def main(args: Array[String]): Unit = {
    // Define the log file path on S3
    setupIceberg()

    val logFilePath = "s3a://pateash-dev/logs"
    // Read the raw text logs from S3
    val logsDf = spark.read.text(logFilePath)

    val transformedDF = Transformer(logsDf)

    transformedDF.show(false)
    Loader(transformedDF)
  }
}
