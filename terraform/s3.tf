# s3.tf
resource "aws_s3_bucket" "raw_logs" {
  bucket = var.s3_bucket_raw_logs
  acl    = "private"
}

resource "aws_s3_bucket" "iceberg_data" {
  bucket = var.s3_bucket_iceberg_data
  acl    = "private"
}
