#!/bin/bash -ex
echo "Validating server"
sleep 30
curl -sSf http://localhost:8080/v2/api-docs
EC2_INSTANCE_ID=$(curl -s http://169.254.169.254/latest/meta-data/instance-id)
EC2_AZ=$(curl -s http://169.254.169.254/latest/meta-data/placement/availability-zone)
echo "Miro backend started on EC2_INSTANCE_ID=${EC2_INSTANCE_ID} $in AZ=${EC2_AZ}"