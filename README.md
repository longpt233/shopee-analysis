# shopee-analysis
bigdata flow example


# How to run 

- pip install -r requirements.txt
- chay crawl 
- docker-compose up

# Note kafka 

- Listeners, advertised listeners, and listener protocols
- KAFKA_LISTENERS :có thể có nhiều, cách nhau bằng dấu , 
```
<protocol>://<host>:<port>
```

```
EXTERNAL_SAME_HOST://:29092,
EXTERNAL_DIFFERENT_HOST://:29093,INTERNAL://:9092
```

- KAFKA_ADVERTISED_LISTENERS : cái trên chỉ là cho broker thôi . muốn client(consummer, producer) kết nối được thì phai chỉ định cái này. 
```
INTERNAL://kafka:9092,
EXTERNAL_SAME_HOST://localhost:29092,
EXTERNAL_DIFFERENT_HOST://157.245.80.232:29093
```

- KAFKA_LISTENER_SECURITY_PROTOCOL_MAP
```
INTERNAL
EXTERNAL_SAME_HOST
PLAINTEXT    *( no auth) 
```

## 1. Crawl

## 2. Chạy producer gửi massage vô kafka 

## 3. Dùng SparkStreaming đọc và đẩy vô HDFS 

> # DEBUG 
- có thể vô master spark để dùng shell đọc kakfa 

- vô master. check job ở http://localhost:8080/
```
docker exec -it -t spark-master bash
```

- vô shell 
```
spark-shell --packages org.apache.spark:spark-sql-kafka-0-10_2.12:3.2.0 --master local 
```

- dọc kafka 

```
val df = spark.readStream.format("kafka").option("kafka.bootstrap.servers", "172.25.0.8:9092").option("subscribe", "hello-kafka").option("startingOffsets", "earliest").load()
val query2 = df.writeStream.format("console").start()

```

> #DEBUG 
- vô kafka read stream

## 4. 



# REF 
- https://spark.apache.org/docs/latest/structured-streaming-kafka-integration.html#deploying
- 