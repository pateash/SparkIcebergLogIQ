package in.pateash.sparkiceberglogiq.listener

import org.apache.spark.sql.execution.QueryExecution
import org.apache.spark.sql.util.QueryExecutionListener

class PerformanceQueryListener extends QueryExecutionListener {
  override def onSuccess(funcName: String, qe: QueryExecution, durationNs: Long): Unit = {
    println(s"Query [$funcName] completed in ${durationNs / 1e6} ms")
  }

  override def onFailure(funcName: String, qe: QueryExecution, exception: Exception): Unit = {
    println(s"Query [$funcName] failed: ${exception.getMessage}")
  }
}
