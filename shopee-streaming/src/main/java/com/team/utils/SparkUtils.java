package com.team.utils;

import com.team.config.Config;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SQLContext;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaStreamingContext;

public class SparkUtils {

    public SparkSession session;
    public JavaSparkContext javaSparkContext;
    public JavaStreamingContext javaStreamingContext;

    public void  createSparkSession(String nameJob, boolean log){
        if (!log) {
            Logger.getLogger("org").setLevel(Level.OFF);
            Logger.getLogger("akka").setLevel(Level.OFF);
        }
        session = SparkSession
                .builder()
                .appName(nameJob)
                // spark config
                .config("spark.debug.maxToStringFields",100)
                .config("spark.speculation", "true")
                .config("spark.executor.extraJavaOptions", "-Xss512m")
                .config("spark.sql.parquet.binaryAsString", "true")
                .config("spark.yarn.access.hadoopFileSystems", "hdfs://" + Config.ACTIVE_NAME_NODE +":9000/") // for read/write HDFS
                .config("spark.serializer","org.apache.spark.serializer.KryoSerializer")
                // 4 executor per instance of each worker
                .config("spark.executor.instances", "4")
                .getOrCreate();
    }

    public void createSparkSessionWithES(String nameJob, boolean log){
        if (!log) {
            Logger.getLogger("org").setLevel(Level.OFF);
            Logger.getLogger("akka").setLevel(Level.OFF);
        }
        session =  SparkSession
                .builder()
                .appName(nameJob)
                // spark config
                .config("spark.debug.maxToStringFields",100)
                .config("spark.speculation", "true")
                .config("spark.executor.extraJavaOptions", "-Xss512m")
                .config("spark.sql.parquet.binaryAsString", "true")
                .config("spark.yarn.access.hadoopFileSystems", "hdfs://" + Config.ACTIVE_NAME_NODE +":9000/") // for read/write HDFS
                .config("spark.serializer","org.apache.spark.serializer.KryoSerializer")
                //  es config
                .config("es.nodes", Config.ELASTICSEARCH_CLUSTER[0])
                .config("es.index.auto.create", "true")
                .config("es.write.operation", "upsert")
                .config("refresh_interval", "30s")
                .config("es.batch.size.entries", "2000")
                .config("es.resource", "_all/types")
                .config("es.cluster.name",Config.ELASTICSEARCH_NAME)
                .config("es.batch.write.retry.count", "3")
                .config("es.batch.write.retry.wait", "10s")
                .getOrCreate();
    }

    public SparkSession getSession() {
        return session;
    }

    public JavaSparkContext getJavaSparkContext() {
        javaSparkContext = new JavaSparkContext(session.sparkContext());
        return javaSparkContext;
    }

    public JavaStreamingContext getJavaStreamingContext(int durationInSecond) {
        javaSparkContext = new JavaSparkContext(session.sparkContext()); // create spark context to create Streaming context
        javaStreamingContext = new JavaStreamingContext(javaSparkContext, Durations.seconds(durationInSecond));
        return javaStreamingContext;
    }

    public void close() {
        session.close();
        javaSparkContext.close();
    }
}
