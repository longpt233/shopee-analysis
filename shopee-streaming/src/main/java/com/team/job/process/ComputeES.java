package com.team.job.process;

import com.team.config.Config;
import com.team.job.model.KeyWithVal;
import com.team.utils.SparkUtils;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.spark.rdd.api.java.JavaEsSpark;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * dùng spark đọc es để tính toán phân tán
 */
public class ComputeES implements Serializable {

    private SparkUtils sparkUtils;

    public ComputeES(){
        sparkUtils = new SparkUtils();
        sparkUtils.createSparkSessionWithES("compute es", false);
    }

    public void compute(){

        // build query
        QueryBuilder matchSpecificFieldQuery= QueryBuilders.matchQuery("k", "2001:df7:c600::/48");

        BoolQueryBuilder qb = QueryBuilders.boolQuery();
        qb.must(matchSpecificFieldQuery);

        // query
        JavaRDD<KeyWithVal> rdd = JavaEsSpark.esJsonRDD(
                sparkUtils.getJavaSparkContext(),
                Config.ELASTICSEARCH_INDEX_NAME+"/",
                new SearchSourceBuilder().query(qb).toString()
        ).flatMap(row -> {

            List<KeyWithVal> kv = new ArrayList();
            String Id = row._1;
            String k="";
            String v= "";

            try {
                JSONObject jsonObject = new JSONObject(row._2);
                if (jsonObject.has("k")) k = jsonObject.getString("k");
                if (jsonObject.has("v")) v = jsonObject.getString("v");

                kv.add(new KeyWithVal(k,v));

            }catch (Exception e){
                e.printStackTrace();
            }
            return kv.iterator();
        });

        Dataset<Row> es = sparkUtils.getSession().sqlContext().createDataset(rdd.rdd(), Encoders.bean(KeyWithVal.class)).toDF().distinct();
        es.show();

    }

    public static void main(String[] args) {
        ComputeES task = new ComputeES();
        task.compute();
    }


}
