# helm.tf
resource "helm_release" "spark" {
  name       = "spark"
  chart      = "bitnami/spark"
  namespace  = "default"
  create_namespace = true

  values = [
    <<EOF
worker:
  replicas: ${var.node_group_size}
EOF
  ]
}

resource "helm_release" "hive" {
  name       = "hive"
  chart      = "bitnami/hive"
  namespace  = "default"

  values = [
    <<EOF
metastore:
  enabled: true
  db:
    driver: "mysql"
    host: "${aws_db_instance.hive_metastore.address}"
    port: 3306
    username: "hive"
    password: "hivepassword123"
EOF
  ]
}