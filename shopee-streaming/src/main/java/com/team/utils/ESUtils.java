package com.team.utils;

import com.team.config.Config;
import org.apache.http.HttpHost;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

import java.io.IOException;

public class ESUtils {

    private RestHighLevelClient client;
    private BulkRequest bulkRequest;

    public ESUtils(){

        int clusterSize = Config.ELASTICSEARCH_CLUSTER.length;

        HttpHost[] httpHost = new HttpHost[clusterSize];
        for(int index=0 ;index< clusterSize;index++){
            httpHost[index] = new HttpHost(Config.ELASTICSEARCH_CLUSTER[index],9200);
        }

        client = new RestHighLevelClient(
                RestClient.builder(httpHost));
    }

    public RestHighLevelClient getClient() {
        return client;
    }

    public void closeConn() {
        try {
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
