package in.pateash.sparkiceberglogiq

import in.pateash.sparkiceberglogiq.Main.{icebergMetricsSchema, icebergLogsTable, spark}
import org.apache.spark.sql.DataFrame

object Loader {

  def apply(df: DataFrame): Unit = {
    // Define the Iceberg table name, storing inside default schema
    writeToIceberg(df)
    IPMetricsLoader(df)
    DeviceMetricsLoader(df)
    showDates(icebergLogsTable)
  }

  def writeToIceberg(df: DataFrame): Unit = {
    df.write
      .format("iceberg")
      .mode("overwrite")
      .partitionBy("date")
      .save(icebergLogsTable)
    // Write the parsed logs DataFrame to the Iceberg table
    //    parsedLogsDf.writeTo("my_catalog.logs_db.parsed_logs").append()
    println("Data successfully written to Iceberg format.")
  }

  def showDates(table: String): Unit = {
    println(s"showing unique dqtes in $table")
    val df = spark.read.format("iceberg")
      .load(table)

    df.select("date").distinct().show()
    df.show()
  }
  def showWeeks(table: String): Unit = {
    println(s"showing unique weeks in $table")
    val df = spark.read.format("iceberg")
      .load(table)

    df.select("week").distinct().show()
    df.show()
  }
}
