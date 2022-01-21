package com.team.job;

import com.team.config.Config;
import com.team.utils.SparkUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import org.apache.spark.streaming.kafka010.ConsumerStrategies;
import org.apache.spark.streaming.kafka010.KafkaUtils;
import org.apache.spark.streaming.kafka010.LocationStrategies;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class PushHDFS {

    public void push(){

        HashMap<String, Object> kafkaParams = new HashMap<>();
        kafkaParams.put("bootstrap.servers", Config.KAFKA_CLUSTER);
        kafkaParams.put("key.deserializer", StringDeserializer.class);
        kafkaParams.put("value.deserializer", StringDeserializer.class);
        kafkaParams.put("group.id", "" + System.currentTimeMillis());
        kafkaParams.put("auto.offset.reset", "latest");
        kafkaParams.put("enable.auto.commit", false);

        Collection<String> topics = new ArrayList<String>();
        topics.add("user_contact");

        SparkUtils sparkUtils = new SparkUtils("push data from kafka to hdfs");
        JavaInputDStream<ConsumerRecord<Object, Object>> stream = KafkaUtils.createDirectStream(
                sparkUtils.getJavaStreamingContext(),
                LocationStrategies.PreferConsistent(),
                ConsumerStrategies.Subscribe(topics, kafkaParams)
        );


    }
}
