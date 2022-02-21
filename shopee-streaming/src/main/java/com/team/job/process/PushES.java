package com.team.job.process;

import com.team.config.Config;
import com.team.utils.ESUtils;
import com.team.utils.SparkUtils;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;

import java.io.IOException;
import java.io.Serializable;

/**
 * job đọc từ hdfs đẩy vô es
 * example compute spark
 */

public class PushES implements Serializable {

    private SparkUtils sparkUtils;

    public PushES(){
        sparkUtils = new SparkUtils();
        sparkUtils.createSparkSession("get and push data",false);
    }

    public XContentBuilder getKVBuilder(Row row) {

        XContentBuilder builder = null;
        try {
            builder = XContentFactory.jsonBuilder()
                    .startObject()
                    .field("k", row.getString(0))
                    .field("v", row.getString(1))
                    .endObject();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return builder;
    }

    public void push(){
        SparkSession sparkSession = sparkUtils.getSession();
        Dataset<Row> df = sparkSession.read().parquet("hdfs://"+ Config.ACTIVE_NAME_NODE+":9000/test/*");
        df.repartition(2).foreachPartition(f->{   // repartition - tinh phan tan tren cum
            ESUtils esUtils = new ESUtils();
            RestHighLevelClient client = esUtils.getClient();
            IndexRequest request = new IndexRequest(Config.ELASTICSEARCH_INDEX_NAME).timeout(TimeValue.timeValueSeconds(30));
            while (f.hasNext()) {

                Row data = f.next();
                request.source(getKVBuilder(data));

                try {
                    IndexResponse response = client.index(request, RequestOptions.DEFAULT);
                    System.out.println("res: "+response);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            esUtils.closeConn();
        });

    }

    public static void main(String[] args) {
        PushES task = new PushES();
        task.push();
    }

}
