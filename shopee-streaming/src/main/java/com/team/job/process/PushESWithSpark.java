package com.team.job.process;

import com.team.config.Config;
import com.team.utils.SparkUtils;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

/**
 * job đọc từ hdfs đẩy vô es
 *
 */

public class PushESWithSpark {

    private SparkUtils sparkUtils;

    public PushESWithSpark(){
        sparkUtils.createSparkSessionWithES("get and push data",false);
    }

    public void push(){
        SparkSession sparkSession = sparkUtils.getSession();
        Dataset<Row> df = sparkSession.read().parquet("hdfs://"+ Config.ACTIVE_NAME_NODE+":9000/test");
        df.show();
        
    }

}
