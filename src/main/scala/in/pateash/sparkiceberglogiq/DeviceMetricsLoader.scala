package in.pateash.sparkiceberglogiq

import in.pateash.sparkiceberglogiq.Loader.{showDates, showWeeks}
import in.pateash.sparkiceberglogiq.Main.{icebergLogsTable, icebergMetricsSchema, spark, top5DevicesDaily, top5DevicesWeekly, top5IpsDailyTable}
import org.apache.spark.sql.DataFrame

object DeviceMetricsLoader {

  def apply(df: DataFrame): Unit = {
    // Define the Iceberg table name, storing inside default schema
    println("Loading Device metrics")
    writeTop5DevicesDaily(df)
    writeTop5DevicesWeekly(df)
  }

  private def writeTop5DevicesDaily(df: DataFrame): Unit = {
    val query =
      s"""INSERT INTO $top5DevicesDaily
         |SELECT
         |  date,
         |  device_type,
         |  COUNT(*) AS request_count
         |FROM $icebergLogsTable
         |GROUP BY date, device_type
         |ORDER BY date, request_count DESC""".stripMargin

    spark.sql(query)
    showDates(top5DevicesDaily)

  }

  private def writeTop5DevicesWeekly(df: DataFrame): Unit = {
    val query =
      s"""INSERT INTO $top5DevicesWeekly
         |SELECT
         |  DATE_TRUNC('WEEK', date) AS week,
         |  device_type,
         |  COUNT(*) AS request_count
         |FROM $icebergLogsTable
         |GROUP BY week, device_type
         |ORDER BY week, request_count DESC""".stripMargin

    spark.sql(query)
    showWeeks(top5DevicesWeekly)
  }
}
