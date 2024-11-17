module "eks" {
  source          = "terraform-aws-modules/eks/aws"
  version         = "19.0.1"
  cluster_name    = var.cluster_name
  cluster_version = "1.27"
  vpc_id          = module.vpc.vpc_id
  subnet_ids      = module.vpc.private_subnets

  self_managed_node_groups = {
    worker_nodes = {
      name           = "worker-nodes"
      instance_types = [var.node_instance_type]
      min_size       = var.node_group_size
      max_size       = var.node_group_size + 2
      desired_size   = var.node_group_size
    }
  }
}
