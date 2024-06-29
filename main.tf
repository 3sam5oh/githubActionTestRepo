provider "aws" {
  region = "ap-northeast-2"
}

resource "aws_instance" "app_server" {
  ami           = "ami-0c55b159cbfafe1f0"  # 서울 리전의 Amazon Linux 2 AMI
  instance_type = "t2.micro"               # 프리 티어 인스턴스 유형

  tags = {
    Name = "AppServer"
  }
}
