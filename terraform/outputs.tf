# outputs.tf
output "eks_cluster_endpoint" {
  value = module.eks.cluster_endpoint
}

output "s3_raw_logs_bucket" {
  value = aws_s3_bucket.raw_logs.bucket
}

output "s3_iceberg_data_bucket" {
  value = aws_s3_bucket.iceberg_data.bucket
}

output "rds_endpoint" {
  value = aws_db_instance.hive_metastore.address
}
