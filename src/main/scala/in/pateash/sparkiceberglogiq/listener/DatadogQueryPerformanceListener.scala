package in.pateash.sparkiceberglogiq.listener

import com.timgroup.statsd.{NonBlockingStatsDClient, NonBlockingStatsDClientBuilder}
import org.apache.spark.sql.util.QueryExecutionListener
import org.apache.spark.sql.execution.QueryExecution

class DatadogQueryPerformanceListener extends QueryExecutionListener {
  // Initialize DogStatsD client using the builder
  private val statsd: NonBlockingStatsDClient = new NonBlockingStatsDClientBuilder()
    .prefix("spark")          // Metric namespace (prefix for all metrics)
    .hostname("localhost")    // Datadog agent host
    .port(8125)               // Datadog StatsD port (default: 8125)
    .constantTags("env:production") // Optional constant tags
    .build()

  override def onSuccess(funcName: String, qe: QueryExecution, durationNs: Long): Unit = {
    val durationMs = durationNs / 1e6
    statsd.incrementCounter("query.success")
    statsd.recordExecutionTime("query.execution.time", durationMs.toLong)
    println(s"Query [$funcName] succeeded in $durationMs ms")
  }

  override def onFailure(funcName: String, qe: QueryExecution, exception: Exception): Unit = {
    statsd.incrementCounter("query.failure")
    println(s"Query [$funcName] failed: ${exception.getMessage}")
  }
}
