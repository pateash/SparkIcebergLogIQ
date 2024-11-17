package in.pateash.sparkiceberglogiq

import in.pateash.sparkiceberglogiq.Main.{createIcebergLogsTableQuery, createIcebergSchemaQuery, spark}
import in.pateash.sparkiceberglogiq.listener.{DatadogQueryPerformanceListener, PerformanceQueryListener}
import org.apache.spark.sql.SparkSession

trait InitSpark extends Constants {
  val spark: SparkSession = SparkSession.builder()
    .appName("SparkIcebergLogIQ Application")
    .master("local[*]") // Adjust for local testing
    .config("spark.hadoop.fs.s3a.impl", "org.apache.hadoop.fs.s3a.S3AFileSystem")
    .config("spark.hadoop.fs.s3a.access.key", awsAccessKey)
    .config("spark.hadoop.fs.s3a.secret.key", awsSecretKey)
    .config("spark.hadoop.fs.s3a.path.style.access", "true")
    .config("spark.hadoop.fs.s3a.debug.enabled", "true")
    //  .config("spark.hadoop.fs.s3a.endpoint", "s3.us-west-2.amazonaws.com") // Adjust for your bucket region
    // Iceberg
    .config("spark.sql.extensions", "org.apache.iceberg.spark.extensions.IcebergSparkSessionExtensions")
    .config("spark.sql.catalog.my_catalog", "org.apache.iceberg.spark.SparkCatalog")
    .config("spark.sql.catalog.my_catalog.type", "hadoop")
    .config("spark.sql.catalog.my_catalog.warehouse", "s3a://pateash-dev/warehouse")

    // logging and monitoring
    .config("spark.eventLog.enabled", "true")
    .config("spark.eventLog.dir", "s3a://pateash-dev/spark-events")
    .config("spark.metrics.namespace", "IcebergMonitoring")
    .getOrCreate()

  // Attach the listener
  spark.listenerManager.register(new PerformanceQueryListener())
  spark.listenerManager.register(new DatadogQueryPerformanceListener())

  def setupIceberg(): Unit = {
    //    spark.sql(f"ALTER TABLE $icebergTable ADD COLUMN device_type STRING")
    spark.sql(createIcebergSchemaQuery)
    spark.sql(createIcebergLogsTableQuery)
    spark.sql(createIcebergIpsDailyQuery)
    spark.sql(createIcebergIpsWeeklyQuery)
    spark.sql(createIcebergDevicesDailyQuery)
    spark.sql(createIcebergDevicesWeeklyQuery)
  }
}
