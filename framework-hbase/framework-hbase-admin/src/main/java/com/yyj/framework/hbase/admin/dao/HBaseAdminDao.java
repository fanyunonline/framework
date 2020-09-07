package com.yyj.framework.hbase.admin.dao;


import com.yyj.framework.hbase.admin.model.HBaseRows;
import com.yyj.framework.hbase.core.client.HBaseClient;

import java.util.List;
import java.util.Set;

/**
 * Created by yangyijun on 2018/3/22.
 */
public interface HBaseAdminDao {

    void setHBaseClient(HBaseClient hBaseClient);

    boolean existsDatabase(String database);

    boolean existsDatabase(String database, String url);

    boolean existsTable(String database, String table);

    boolean existsTable(String database, String table, String url);

    boolean createDatabase(String database);

    boolean createDatabase(String database, String url);

    boolean createTable(String database, String table, boolean preBuildRegion);

    boolean createTable(String database, String table, List<String> families, boolean preBuildRegion);

    boolean createTable(String database, String table, String url, boolean preBuildRegion);

    boolean createTable(String database, String table, String url, List<String> families, boolean preBuildRegion);

    boolean deleteTable(String database, String table);

    boolean deleteTable(String database, String table, String url);

    boolean addCoprocessor(String database, String table, String className);

    boolean addCoprocessor(String database, String table, String url, String className);

    boolean addAggCoprocessor(String database, String table, String url);

    boolean bulkUpsert(String tableName, HBaseRows rows);

    boolean bulkUpsert(String tableName, String url, HBaseRows rows);

    boolean deleteByRowKeys(String tableName, Set<String> rowKeys);

    boolean deleteByRowKeys(String tableName, String url, Set<String> rowKeys);

    void deleteByScan(String tableName, String startRow, String stopRow);

    List<String> listTableNames(String regex);
}
