package com.yyj.framework.hbase.search.dao.impl;

import com.yyj.framework.common.util.log.Log;
import com.yyj.framework.common.util.log.LogFactory;
import com.yyj.framework.hbase.core.client.HBaseClient;
import com.yyj.framework.hbase.core.util.CloseableUtils;
import com.yyj.framework.hbase.search.dao.HBaseSearchDao;
import com.yyj.framework.hbase.search.model.HBaseSearchQo;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by yagnyijun on 2018/12/04.
 */
@Repository
public class HBaseSearchDaoImpl implements HBaseSearchDao {

    private static final Log logger = LogFactory.getLogger(HBaseSearchDaoImpl.class);

    @Autowired
    private HBaseClient hBaseClient;

    private Connection getConnection(String url) {
        if (url == null) {
            return hBaseClient.getConnection();
        }
        return hBaseClient.getConnection(url);
    }

    @Override
    public List<Map<String, String>> getByRowKeys(HBaseSearchQo hBaseSearchQo) {
        return this.getByRowKeys(hBaseSearchQo.getDatabase() + ":" + hBaseSearchQo.getTable(), hBaseSearchQo.getUrl(), hBaseSearchQo.getRowKeys());
    }


    ///////////////////////
    // private functions
    ///////////////////////
    private List<Map<String, String>> getByRowKeys(String tableName, String url, Set<String> rowKeys) {
        List<Map<String, String>> rows = new ArrayList<>();
        Table hTable = null;
        try {
            hTable = this.getConnection(url).getTable(TableName.valueOf(tableName));
            List<Get> gets = rowKeys.stream().map(rowKey ->
                    new Get(Bytes.toBytes(rowKey))).collect(Collectors.toList());
            Result[] results = hTable.get(gets);
            if (results == null || results.length == 0) {
                return rows;
            }

            // results
            for (int i = 0; i < results.length; i++) {
                Result result = results[i];
                Map<String, String> row = new HashMap<>();
                for (int j = 0; j < result.rawCells().length; j++) {
                    Cell cell = result.rawCells()[j];
                    row.put(Bytes.toString(CellUtil.cloneQualifier(cell)),
                            Bytes.toString(CellUtil.cloneValue(cell)));
                }
                rows.add(row);
            }
        } catch (Exception e) {
            logger.error(e);
        } finally {
            CloseableUtils.close(hTable);
        }
        return rows;
    }
}
