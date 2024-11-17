package in.pateash.sparkiceberglogiq

import in.pateash.sparkiceberglogiq.Main.{icebergLogsTable, icebergMetricsSchema, spark, top5AipsWeekly, top5IpsDailyTable}
import org.apache.spark.sql.DataFrame
import in.pateash.sparkiceberglogiq.Loader
import in.pateash.sparkiceberglogiq.Loader.{showDates, showWeeks}

object IPMetricsLoader {

  def apply(df: DataFrame): Unit = {
    // Define the Iceberg table name, storing inside default schema
    println("Loading IP metric tables")
    writeTop5IpsDaily(df)
    writeTop5IpsWeekly(df)
  }

  private def writeTop5IpsDaily(df: DataFrame): Unit = {
    val query =
      s"""INSERT INTO $top5IpsDailyTable
         |SELECT
         |  date,
         |  ip,
         |  COUNT(*) AS request_count
         |FROM $icebergLogsTable
         |GROUP BY date, ip
         |ORDER BY date, request_count DESC""".stripMargin
    spark.sql(query)

    showDates(top5IpsDailyTable)
  }

  private def writeTop5IpsWeekly(df: DataFrame): Unit = {
    val query =
      s"""INSERT INTO $top5AipsWeekly
         |SELECT
         |  DATE_TRUNC('WEEK', date) AS week,
         |  ip,
         |  COUNT(*) AS request_count
         |FROM $icebergLogsTable
         |GROUP BY week, ip
         |ORDER BY week, request_count DESC""".stripMargin
    spark.sql(query)
    showWeeks(top5AipsWeekly)
  }
}
