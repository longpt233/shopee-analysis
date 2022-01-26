package com.team.job.process;

import com.team.config.Config;
import com.team.job.model.KeyWithVal;
import com.team.utils.SparkUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.spark.TaskContext;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.*;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.sql.catalyst.encoders.RowEncoder;
import org.apache.spark.storage.StorageLevel;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka010.*;
import scala.Tuple2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class PushHDFS {
    private SparkUtils sparkUtils;

    public PushHDFS(){
        sparkUtils = new SparkUtils();
        sparkUtils.createSparkSession("job streaming to hdfs", false);
    }

    public void push() throws InterruptedException {

        HashMap<String, Object> kafkaParams = new HashMap<>();
        kafkaParams.put("bootstrap.servers", Config.KAFKA_DOCKER);
        kafkaParams.put("key.deserializer", StringDeserializer.class);
        kafkaParams.put("value.deserializer", StringDeserializer.class);
        kafkaParams.put("group.id", "" + System.currentTimeMillis());
        kafkaParams.put("auto.offset.reset", "earliest");
        kafkaParams.put("enable.auto.commit", false);

        Collection<String> topics = new ArrayList<String>();
        topics.add(Config.KAFKA_TOPIC);

        JavaStreamingContext javaStreamingContext = sparkUtils.getJavaStreamingContext(60);

        JavaInputDStream<ConsumerRecord<String, String>> stream = KafkaUtils.createDirectStream(
                javaStreamingContext,
                LocationStrategies.PreferConsistent(),
                ConsumerStrategies.Subscribe(topics, kafkaParams)
        );

//        JavaDStream<String> data = stream.map(v -> { return v.value(); });
//        data.print();

        stream.foreachRDD(rdd ->{
            System.out.println("nhan dc 1 rdd= "+ rdd);
            System.out.println("collect");

            // collect rdd in 1 duration
            JavaPairRDD<String, String> rddKafkaMes = rdd.mapToPair(f -> {
                System.out.println(f.key());
                return new Tuple2<String, String>(f.key(), f.value());
            });

            System.out.println("process");
            // neu co data -> xu li
            if (rddKafkaMes.count() != 0) {
                System.out.println("size kafka = "+ rddKafkaMes.count());
                JavaRDD<KeyWithVal> jrdd = rddKafkaMes.map(f -> {
                    String key = f._1;
                    String val = f._2;
                    return new KeyWithVal(key,val);
                });


                Dataset<KeyWithVal> df = sparkUtils.getSession().sqlContext().createDataset(jrdd.rdd(), Encoders.bean(KeyWithVal.class));
                df.show();
                df.write().mode(SaveMode.Append).parquet("hdfs://"+ Config.ACTIVE_NAME_NODE+":9000/test");

            }
        });

        System.out.println("start stream !!");
        javaStreamingContext.start();
        javaStreamingContext.awaitTermination();

    }

    public static void main(String[] args) throws InterruptedException {
        PushHDFS pushHDFS = new PushHDFS();
        pushHDFS.push();
    }
}
