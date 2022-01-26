#!/bin/bash

docker cp ./target/shopee-streaming-1.0-SNAPSHOT-jar-with-dependencies.jar \
  spark-master-name:/opt/bitnami/spark/examples/jars/shopee-streaming-1.0-SNAPSHOT-jar-with-dependencies.jar