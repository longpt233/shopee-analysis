package com.team.job.eval;

import org.apache.kafka.clients.consumer.*;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

public class TestReadKafka {

    public void readKafka(){
        Properties prop = new Properties();

        prop.put("bootstrap.servers", "localhost:29092");
        prop.setProperty("group.id", "test");
        prop.setProperty("enable.auto.commit", "true");
        prop.setProperty("auto.commit.interval.ms", "1000");
        prop.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        prop.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        prop.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");   // or latest

        try (Consumer<String, String> consumer = new KafkaConsumer<String, String>(prop)) {
            consumer.subscribe(Arrays.asList("hello-kafka"));
            while(true) {
                ConsumerRecords<String, String> records = consumer.poll(100);
                for(ConsumerRecord<String, String> record: records) {
                    System.out.println("offset = " + record.offset() + ", key = " + record.key() + ", "  + ", value = " + record.value());
                }
            }
        }
    }

    public static void main(String[] args) {
        TestReadKafka testReadKafka = new TestReadKafka();
        testReadKafka.readKafka();
    }
}
