package in.pateash.sparkiceberglogiq

import in.pateash.sparkiceberglogiq._
import in.pateash.sparkiceberglogiq.Main.spark
import org.apache.spark.sql.functions._

object Query extends InitSpark with Constants {
  def main(args: Array[String]): Unit = {
    // Define the log file path on S3
    import spark.implicits._
        val table = top5IpsDailyTable
//    val table = top5AipsWeekly
    //    val table = top5DevicesDaily
    //    val table = top5DevicesWeekly


    val df = spark.read.format("iceberg")
      .load(table)
    df.show(false)

    // top ip/device in week

//    val finalDf = df.groupBy($"week")
    val finalDf = df.groupBy($"date")
      .agg(max($"request_count").as("max_requests"))

//        val finalDf = df.groupBy($"week")
//      .agg(max($"request_count").as("max_requests"))
    //    val finalDf = df.filter()
        finalDf.show(false)
  }
}
