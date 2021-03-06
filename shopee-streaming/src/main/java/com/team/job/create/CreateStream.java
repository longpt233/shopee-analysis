package com.team.job.create;

import com.team.config.Config;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * send message to kafka
 * TODO : viết job scrapy streaming luôn
 * */
public class CreateStream {

    public void initFlow(){
        Properties prop = new Properties();

        prop.put("bootstrap.servers", Config.KAFKA);
        prop.put("retries", 0);
        prop.put("key.serializer", StringSerializer.class);
        prop.put("value.serializer", StringSerializer.class);

        Producer<String, String> producer = new KafkaProducer<>(prop);

        List<String> data = getDataByRow(Config.DATA_DIR);

        for(String i : data) {
            String key = i.split("\t")[0];
            String val = i.split("\t")[1];
            producer.send(new ProducerRecord<>(Config.KAFKA_TOPIC, key, val));
        }

        System.out.println("send data successfully!");
        producer.close();
    }

    public List<String> getDataByRow(String path ) {
        List<String> res = new ArrayList<>();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(path));
            String line;
            while ((line = reader.readLine()) != null ) {
                res.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return res;

    }

    public static void main(String[] args) {
        CreateStream createStream = new CreateStream();
        createStream.initFlow();
    }


}
