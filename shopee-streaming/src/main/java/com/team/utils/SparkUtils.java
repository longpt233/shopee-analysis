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
    public SQLContext sqlContext;

    public SparkUtils(String nameJob) {
        System.out.println("Create spark");
        session = createSparkConfig(nameJob, false, true);
        javaSparkContext = new JavaSparkContext(session.sparkContext());
        javaStreamingContext = new JavaStreamingContext(javaSparkContext, Durations.seconds(30));
        sqlContext = session.sqlContext();
        session.sparkContext().setLogLevel("ERROR");
        System.out.println("Create Done!!");
    }

    public SparkSession createSparkConfig(String nameJob, boolean log, boolean master){
        if (!log) {
            Logger.getLogger("org").setLevel(Level.OFF);
            Logger.getLogger("akka").setLevel(Level.OFF);
        }

        return SparkSession
                .builder()
                .appName(nameJob)
                .config("spark.debug.maxToStringFields",100)
                .config("spark.speculation", "true")
                .config("spark.executor.extraJavaOptions", "-Xss512m")
                .config("spark.sql.parquet.binaryAsString", "true")
                .config("spark.yarn.access.hadoopFileSystems", "hdfs://" + Config.ACTIVE_NAME_NODE +":9000/")
                .config("spark.serializer","org.apache.spark.serializer.KryoSerializer")
                .config("es.nodes", Config.ELASTICSEARCH_CLUSTER[0])
                .config("es.index.auto.create", "true")
                .config("es.write.operation", "upsert")
                .config("refresh_interval", "30s")
                .config("es.batch.size.entries", "2000")
                .config("es.resource", "_all/types")
                .config("es.cluster.name","kinghub")
                .config("es.batch.write.retry.count", "3")
                .config("es.batch.write.retry.wait", "10s")
                .getOrCreate();

    }

    public SparkSession getSession() {
        return session;
    }

    public JavaSparkContext getJavaSparkContext() {
        return javaSparkContext;
    }

    public JavaStreamingContext getJavaStreamingContext() {
        return javaStreamingContext;
    }

    public SQLContext getSqlContext() {
        return sqlContext;
    }

    public void close() {
        session.close();
        javaSparkContext.close();
    }
}
