package com.team.config;

public class Config {

    // LOCAL
    public static final String DATA_DIR = "data/example.txt";

    // HDFS
    public static final String ACTIVE_NAME_NODE = "hadoop-master";  // khong de ip dc vi no tu phan dai ra host -> loi

    // ES
    public static final String[] ELASTICSEARCH_CLUSTER = {"es-host"};   // or localhost for test
    public static final String ELASTICSEARCH_INDEX_NAME = "test";

    // KAFKA
    public static final String KAFKA = "localhost:29092";   // co the la ca mot cluster, dia chi ngoai docker
    public static final String KAFKA_DOCKER = "kafka-host:9092";  // dia chi noi bo docker
    public static final String KAFKA_TOPIC = "hello-kafka";



}
