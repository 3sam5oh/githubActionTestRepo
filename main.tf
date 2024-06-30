provider "aws" {
  region = "ap-northeast-2"
}

resource "aws_opensearch_domain" "search_engine" {
  domain_name           = "search-engine"
  engine_version        = "OpenSearch_2.13" # OpenSearch 버전을 명시합니다.

  cluster_config {
    instance_type = "t2.small.search"  # 프리 티어 인스턴스 유형
    instance_count = 1
  }

  ebs_options {
    ebs_enabled = true
    volume_size = 10
    volume_type = "gp2"
  }

  access_policies = <<EOF
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Principal": {
        "AWS": "*"
      },
      "Action": "es:*",
      "Resource": "arn:aws:es:ap-northeast-2:123456789012:domain/search-engine/*",
      "Condition": {
        "IpAddress": {
          "aws:SourceIp": "0.0.0.0/0"
        }
      }
    }
  ]
}
EOF

  advanced_options = {
    "rest.action.multi.allow_explicit_index" = "true"
  }

  tags = {
    Domain = "search-engine"
  }
}
