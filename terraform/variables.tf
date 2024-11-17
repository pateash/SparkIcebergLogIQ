# AWS Region
variable "region" {
  description = "The AWS region to deploy resources in"
  default     = "us-east-1"
}

# EKS Cluster Name
variable "cluster_name" {
  description = "Name of the EKS cluster"
  default     = "spark-eks-cluster"
}

# Node Group Configuration
variable "node_group_name" {
  description = "Name of the worker node group"
  default     = "spark-worker-nodes"
}

variable "node_instance_type" {
  description = "EC2 instance type for worker nodes"
  default     = "t3.large"
}

variable "node_group_size" {
  description = "Number of desired worker nodes"
  default     = 3
}

# S3 Buckets
variable "s3_bucket_raw_logs" {
  description = "S3 bucket name for raw logs"
  default     = "raw-logs-bucket"
}

variable "s3_bucket_iceberg_data" {
  description = "S3 bucket name for Iceberg data"
  default     = "iceberg-data-bucket"
}

# Kubernetes Provider Variables
variable "kubernetes_cluster_name" {
  description = "Name of the Kubernetes cluster for provider configuration"
  default     = "spark-eks-cluster"
}

variable "kubernetes_cluster_endpoint" {
  description = "Endpoint of the EKS cluster for Kubernetes provider"
  type        = string
}

variable "kubernetes_ca_certificate" {
  description = "Base64 encoded certificate authority for the Kubernetes cluster"
  type        = string
}

variable "kubernetes_token" {
  description = "Authentication token for the Kubernetes provider"
  type        = string
}
