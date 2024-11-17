package in.pateash.sparkiceberglogiq

import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.functions.{col, regexp_extract, to_date, when}

object Transformer {
  def apply(df: DataFrame): DataFrame = {
    // Define regex patterns for parsing log fields
    val ipPattern = "(\\d+\\.\\d+\\.\\d+\\.\\d+)"
    val timestampPattern = "\\[(.*?)\\]"
    val requestPattern = "\"(GET|POST) (.*?) HTTP/1\\.1\""
    val statusPattern = "\" (\\d{3}) "
    val userAgentPattern = "\" \"(Mozilla.*?)\""

    // Parse each log line to extract fields
    val parsedDF = df
      .withColumn("ip", regexp_extract(col("value"), ipPattern, 1))
      .withColumn("timestamp", regexp_extract(col("value"), timestampPattern, 1))
      .withColumn("request", regexp_extract(col("value"), requestPattern, 2))
      .withColumn("status", regexp_extract(col("value"), statusPattern, 1))
      .withColumn("user_agent", regexp_extract(col("value"), userAgentPattern, 1))
      .withColumn("date", to_date(col("timestamp"), "dd/MMM/yyyy:HH:mm:ss Z"))
      .withColumn("device_type",
        when(col("user_agent").contains("iPad"), "Tablet")
          .when(col("user_agent").contains("Mobile"), "Mobile")
          .when(col("user_agent").contains("Android"), "Mobile")
          .when(col("user_agent").contains("Macintosh"), "Desktop")
          .when(col("user_agent").contains("Windows"), "Desktop")
          .otherwise("Unknown")
      )

      .drop("value") // Drop the original raw log line

    // Drop malformed or invalid rows
    parsedDF.filter(col("ip").isNotNull && col("timestamp").isNotNull)
  }
}
