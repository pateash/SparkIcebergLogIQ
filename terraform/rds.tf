# rds.tf
resource "aws_db_instance" "hive_metastore" {
  allocated_storage    = 20
  engine               = "mysql"
  engine_version       = "8.0"
  instance_class       = "db.t3.medium"
  name                 = "hive_metastore"
  username             = "hive"
  password             = "hivepassword123"
  publicly_accessible  = false
  skip_final_snapshot  = true
  db_subnet_group_name = aws_db_subnet_group.rds_subnet.name
}

resource "aws_db_subnet_group" "rds_subnet" {
  name       = "rds-subnet-group"
  subnet_ids = module.vpc.private_subnets
}
