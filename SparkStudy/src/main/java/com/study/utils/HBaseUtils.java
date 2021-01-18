package com.study.utils;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;

public class HBaseUtils {
    Admin admin = null;
    Configuration conf = HBaseConfiguration.create();
    private HBaseUtils() throws IOException {
        conf.set("hbase.zookeeper.quorum","localhost:2181");
        conf.set("hbase.rootdir","hdfs://localhost:9000/hbase");
        Connection connection = ConnectionFactory.createConnection(conf);
        admin = connection.getAdmin();
    }

    private static HBaseUtils instance = null;
    public static synchronized HBaseUtils getInstance() throws IOException {
        if(null==instance){
            instance = new HBaseUtils();
        }
        return instance;
    }
    //根据表名获取到Htable实例
    public Table getTable(String tableName) throws IOException {
        conf.set("hbase.zookeeper.quorum","localhost:2181");
        conf.set("hbase.rootdir","hdfs://localhost:9000/hbase");
        Connection connection = ConnectionFactory.createConnection(conf);
        Table table = connection.getTable(TableName.valueOf(tableName));

        return table;
    }
    /*
      添加数据到hbase里面
     */
    public void put(String tableName, String rowkey, String cf, String column, String value) throws IOException {
        Table table = getTable(tableName);
        Put put = new Put(Bytes.toBytes(rowkey));
        put.addColumn(Bytes.toBytes(cf),Bytes.toBytes(column),Bytes.toBytes(value));
        try {
            table.put(put);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        String tableName = "click_count";
        String rowkey = "20210113";
        String cf = "info";
        String column = "category_click_count";
        String value = "20";
        HBaseUtils.getInstance().put(tableName,rowkey,cf,column,value);
    }
}
