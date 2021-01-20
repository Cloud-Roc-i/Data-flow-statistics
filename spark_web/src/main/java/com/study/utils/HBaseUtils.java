package com.study.utils;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.PrefixFilter;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
    public Table getTable(String tableName) throws IOException {
        Connection connection = ConnectionFactory.createConnection(conf);
        Table table = connection.getTable(TableName.valueOf(tableName));
        connection.close();
        return table;
    }

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

    public Map<String,Long> query(String tableName, String condition) throws IOException{

        Map<String,Long> map = new HashMap<>();
        Table table = getTable(tableName);
        String cf = "info";
        String qualifier = "click_count";

        Scan scan =  new Scan();

        Filter filter = new PrefixFilter(Bytes.toBytes(condition));
        scan.setFilter(filter);

        ResultScanner rs = table.getScanner(scan);

        for(Result result:rs){
            String row = Bytes.toString(result.getRow());
            Long clickCount = Bytes.toLong(result.getValue(cf.getBytes(),qualifier.getBytes()));
            map.put(row,clickCount);
        }

        return map;
    }

}
