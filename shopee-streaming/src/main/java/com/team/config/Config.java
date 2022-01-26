package com.team.config;

public class Config {

    // LOCAL
    public static final String DATA_DIR = "data/example.txt";
    // HDFS
    public static final String ACTIVE_NAME_NODE = "localhost";

    // ES
    public static final String[] ELASTICSEARCH_CLUSTER = {"localhost"};
    public static final String ELASTICSEARCH_NAME = "";

    // KAFKA
    public static final String KAFKA = "localhost:29092";   // co the la ca mot cluster
    public static final String KAFKA_DOCKER = "kafka-host:9092";
    public static final String KAFKA_TOPIC = "hello-kafka";



}
