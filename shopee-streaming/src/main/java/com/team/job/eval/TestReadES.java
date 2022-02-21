package com.team.job.eval;

import com.team.config.Config;
import com.team.utils.ESUtils;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.IOException;

public class TestReadES {

    private ESUtils esUtils;

    public TestReadES(){
        esUtils = new ESUtils();
    }

    public void getOne(){

        RestHighLevelClient client = esUtils.getClient();

        GetRequest getRequest = new GetRequest(Config.ELASTICSEARCH_INDEX_NAME);
        getRequest.id("tgnDF38BsesTlpGbYY0r");

        try {
            GetResponse getResponse = client.get(getRequest, RequestOptions.DEFAULT);
            System.out.println(getResponse);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void testSearch(){

        RestHighLevelClient client = esUtils.getClient();

        // build query
        SearchSourceBuilder builder = new SearchSourceBuilder()
                .postFilter(QueryBuilders.rangeQuery("age").from(5).to(15));

        // build req
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.searchType(SearchType.DFS_QUERY_THEN_FETCH);
        searchRequest.source(builder);

        // send req
        try {
            SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
            System.out.println("res: "+response);

            SearchHit[] searchHits = response.getHits().getHits();
            for(SearchHit i : searchHits){
                System.out.println(i.getSourceAsString());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void testSearchBuilder(){

        RestHighLevelClient client = esUtils.getClient();

        // build query
        QueryBuilder range = QueryBuilders.rangeQuery("age").from(5).to(15);
        QueryBuilder matchSpecificFieldQuery= QueryBuilders.matchQuery("fullName", "John Doe");

        BoolQueryBuilder qb = QueryBuilders.boolQuery();
        qb.must(range);
        qb.must(matchSpecificFieldQuery);

        SearchSourceBuilder builder = new SearchSourceBuilder().query(qb);

        // build req
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.searchType(SearchType.DFS_QUERY_THEN_FETCH);
        searchRequest.source(builder);

        // send req
        try {
            SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);

            SearchHit[] searchHits = response.getHits().getHits();
            for(SearchHit i : searchHits){
                System.out.println(i.getSourceAsString());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeConn(){
        esUtils.closeConn();
    }

    public static void main(String[] args) {
        TestReadES task = new TestReadES();
        task.testSearch();
        task.getOne();
        task.testSearchBuilder();
        task.closeConn();
    }

}
