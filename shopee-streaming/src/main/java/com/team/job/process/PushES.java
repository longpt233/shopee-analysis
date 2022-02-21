package com.team.job.process;

import com.team.config.Config;
import com.team.utils.ESUtils;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;

import java.io.IOException;

public class PushES {

    private ESUtils esUtils;

    public PushES(){
        esUtils = new ESUtils();
    }

    public String getDataTest(){
        return "{\"age\":10,\"dateOfBirth\":1471466076564,"
                +"\"fullName\":\"John Doe\"}";
    }

    public XContentBuilder getDataTestBuilder() {

        XContentBuilder builder = null;
        try {
            builder = XContentFactory.jsonBuilder()
                    .startObject()
                    .field("fullName", "Test")
                    .field("dateOfBirth", "1471466076566")
                    .field("age", "10")
                    .endObject();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return builder;
    }

    public void push(){

        RestHighLevelClient client = esUtils.getClient();
        IndexRequest request = new IndexRequest(Config.ELASTICSEARCH_INDEX_NAME).timeout(TimeValue.timeValueSeconds(30));

//        request.source(getDataTest(), XContentType.JSON);
        request.source(getDataTestBuilder());

        try {
            IndexResponse response = client.index(request, RequestOptions.DEFAULT);
            System.out.println("res: "+response);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                client.close();   // không đóng conn thì chương trình không thoát
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public static void main(String[] args) {
        PushES task = new PushES();
        task.push();

    }

}
