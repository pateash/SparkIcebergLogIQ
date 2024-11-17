package in.pateash.sparkiceberglogiq

trait Constants {
  val catalogName = "myCatalog"
  val awsAccessKey = ""
  val awsSecretKey = ""
  val icebergLogsSchema = "my_catalog.logs_db"
  val icebergMetricsSchema = "my_catalog.metrics"
  val icebergLogsTable = s"$icebergLogsSchema.logs_iceberg"

  val top5IpsDailyTable = s"$icebergMetricsSchema.top_5_ips_daily"
  val top5AipsWeekly = s"$icebergMetricsSchema.top_5_ips_weekly"

  val top5DevicesDaily = s"$icebergMetricsSchema.top_5_devices_daily"
  val top5DevicesWeekly = s"$icebergMetricsSchema.top_5_devices_weekly"

  val createIcebergSchemaQuery: String =
    s"""CREATE SCHEMA IF NOT EXISTS $icebergLogsSchema""".stripMargin

  val createIcebergIpsDailyQuery: String =
    s"""
       |CREATE TABLE IF NOT EXISTS $icebergMetricsSchema.top_5_ips_daily (
       |  date DATE,
       |  ip STRING,
       |  request_count BIGINT
       |)
       |USING iceberg
       |PARTITIONED BY (date);""".stripMargin

  val createIcebergIpsWeeklyQuery: String =
    s"""CREATE TABLE IF NOT EXISTS $icebergMetricsSchema.top_5_ips_weekly (
       |  week DATE,
       |  ip STRING,
       |  request_count BIGINT
       |)
       |USING iceberg
       |PARTITIONED BY (week);""".stripMargin

  val createIcebergDevicesDailyQuery: String =
    s"""
       |CREATE TABLE IF NOT EXISTS $icebergMetricsSchema.top_5_devices_daily (
       |  date DATE,
       |  device_type STRING,
       |  request_count BIGINT
       |)
       |USING iceberg
       |PARTITIONED BY (date);""".stripMargin

  val createIcebergDevicesWeeklyQuery: String =
    s"""CREATE TABLE IF NOT EXISTS $icebergMetricsSchema.top_5_devices_weekly (
       |  week DATE,
       |  device_type STRING,
       |  request_count BIGINT
       |)
       |USING iceberg
       |PARTITIONED BY (week);""".stripMargin

  val createIcebergLogsTableQuery: String =
    s"""
  CREATE TABLE IF NOT EXISTS $icebergLogsTable (
    ip STRING,
    timestamp STRING,
    request STRING,
    status STRING,
    user_agent STRING,
    date DATE
  )
  USING iceberg
  PARTITIONED BY (date)
""".stripMargin
}
