package com.yyj.framework.hbase.admin.dao.impl;

import com.yyj.framework.common.util.log.Log;
import com.yyj.framework.common.util.log.LogFactory;
import com.yyj.framework.hbase.admin.dao.HBaseAdminDao;
import com.yyj.framework.hbase.admin.model.HBaseRows;
import com.yyj.framework.hbase.core.client.HBaseClient;
import com.yyj.framework.hbase.core.util.CloseableUtils;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.*;

/**
 * Created by yangyijun on 2018/3/22.
 */
@Repository
public class HBaseAdminDaoImpl implements HBaseAdminDao {

    private static final Log logger = LogFactory.getLogger(HBaseAdminDaoImpl.class);

    @Autowired
    private HBaseClient hBaseClient;

    private Connection getConnection() {
        return getConnection(null);
    }

    private Connection getConnection(String url) {
        if (url == null) {
            return hBaseClient.getConnection();
        }
        return hBaseClient.getConnection(url);
    }

    @Override
    public void setHBaseClient(HBaseClient hBaseClient) {
        this.hBaseClient = hBaseClient;
    }

    @Override
    public boolean existsDatabase(String database) {
        return existsDatabase(database, null);
    }

    @Override
    public boolean existsDatabase(String database, String url) {
        boolean exists = false;
        HBaseAdmin admin = null;
        try {
            admin = (HBaseAdmin) this.getConnection(url).getAdmin();
            NamespaceDescriptor descriptor = admin.getNamespaceDescriptor(database);
            if (descriptor != null) {
                exists = true;
            }
        } catch (NamespaceNotFoundException ex) {
            logger.error(ex);
        } catch (IOException e) {
            logger.error(e);
        } finally {
            CloseableUtils.close(admin);
        }
        return exists;
    }

    @Override
    public boolean existsTable(String database, String table) {
        return this.existsTable(database, table, null);
    }

    @Override
    public boolean existsTable(String database, String table, String url) {
        boolean exists = false;
        HBaseAdmin admin = null;
        try {
            admin = (HBaseAdmin) this.getConnection(url).getAdmin();
            exists = admin.tableExists(TableName.valueOf(database, table));
        } catch (IOException e) {
            logger.error(e);
        } finally {
            CloseableUtils.close(admin);
        }
        return exists;
    }

    @Override
    public boolean createDatabase(String database) {
        return this.createDatabase(database, null);
    }

    @Override
    public boolean createDatabase(String database, String url) {
        boolean success = false;
        HBaseAdmin admin = null;
        try {
            admin = (HBaseAdmin) this.getConnection(url).getAdmin();
            NamespaceDescriptor namespaceDescriptor = NamespaceDescriptor.create(database).build();
            admin.createNamespace(namespaceDescriptor);
            success = true;
            logger.info("Success to create database[{0}]", database);
        } catch (IOException e) {
            logger.error("Failed to create database[{0}]\n", e, database);
        } finally {
            CloseableUtils.close(admin);
        }
        return success;
    }

    @Override
    public boolean createTable(String database, String table, boolean preBuildRegion) {
        return this.createTable(database, table, Arrays.asList(HBaseRows.DEFAULT_FAMILY), preBuildRegion);
    }

    @Override
    public boolean createTable(String database, String table, List<String> families, boolean preBuildRegion) {
        return this.createTable(database, table, null, families, preBuildRegion);
    }

    @Override
    public boolean createTable(String database, String table, String url, boolean preBuildRegion) {
        return this.createTable(database, table, url, Arrays.asList(HBaseRows.DEFAULT_FAMILY), preBuildRegion);
    }

    @Override
    public boolean createTable(String database, String table, String url, List<String> families, boolean
            preBuildRegion) {
        boolean success = false;
        HBaseAdmin admin = null;
        try {
            admin = (HBaseAdmin) this.getConnection(url).getAdmin();
            boolean exists = admin.tableExists(TableName.valueOf(database, table));
            if (exists) {
                logger.info("Table[{0}:{1}] is already exists.", database, table);
                return true;
            }

            HTableDescriptor tableDesc = new HTableDescriptor(TableName.valueOf(database, table));
            for (String family : families) {
                HColumnDescriptor columnDescriptor = new HColumnDescriptor(family);
                columnDescriptor.setMaxVersions(3);
                tableDesc.addFamily(columnDescriptor);
            }

            // create
            if (preBuildRegion) {
                admin.createTable(tableDesc, new byte[][]{});
            } else {
                admin.createTable(tableDesc);
            }
            success = true;
            logger.info("Success to create table[{0}:{1}]", database, table);
        } catch (IOException e) {
            logger.error("Failed to create table[{0}/{1}]\n", e, database, table);
        } finally {
            CloseableUtils.close(admin);
        }
        return success;
    }

    @Override
    public boolean deleteTable(String database, String table) {
        return this.deleteTable(database, table, null);
    }

    @Override
    public boolean deleteTable(String database, String table, String url) {
        boolean success = false;
        HBaseAdmin admin = null;
        try {
            admin = (HBaseAdmin) this.getConnection(url).getAdmin();
            TableName tableName = TableName.valueOf(database, table);
            admin.disableTable(tableName);
            admin.deleteTable(tableName);
            success = true;
            logger.info("Success to delete table[{0}:{1}]", database, table);
        } catch (IOException e) {
            logger.error("Failed to delete table[{0}/{1}]\n", e, database, table);
        } finally {
            CloseableUtils.close(admin);
        }
        return success;
    }

    @Override
    public boolean addCoprocessor(String database, String table, String className) {
        return this.addCoprocessor(database, table, className, null);
    }

    @Override
    public boolean addCoprocessor(String database, String table, String url, String className) {
        boolean success = false;
        HBaseAdmin admin = null;
        try {
            admin = (HBaseAdmin) this.getConnection(url).getAdmin();
            TableName tableName = TableName.valueOf(database, table);

            HTableDescriptor tableDesc = admin.getTableDescriptor(tableName);
            if (tableDesc.hasCoprocessor(className)) {
                logger.info("Table[{0}:{1}] coprocessor is already exists, {2}", database, table, className);
                return true;
            }
            admin.disableTable(tableName);
            tableDesc.addCoprocessor(className);
            admin.modifyTable(tableName, tableDesc);
            admin.enableTable(tableName);
            success = true;
            logger.info("Success to add coprocessor table[{0}:{1}], {2}", database, table, className);
        } catch (IOException e) {
            logger.error("Failed to add coprocessor table[{0}/{1}], {2}\n", e, database, table, className);
        } finally {
            CloseableUtils.close(admin);
        }
        return success;
    }

    @Override
    public boolean addAggCoprocessor(String database, String table, String url) {
        return this.addCoprocessor(database, table, url, null);
    }

    @Override
    public boolean bulkUpsert(String tableName, HBaseRows rows) {
        return bulkUpsert(tableName, null, rows);
    }

    @Override
    public boolean bulkUpsert(String tableName, String url, HBaseRows rows) {
        boolean success = false;
        BufferedMutator mutator = null;
        try {
            // puts
            List<Put> puts = new ArrayList<>();
            String family = rows.getFamily();
            for (Map<String, Object> row : rows.getRows()) {
                Put put = new Put(Bytes.toBytes(row.get("rowKey").toString()));
                for (Map.Entry<String, Object> entry : row.entrySet()) {
                    String field = entry.getKey();
                    Object value = entry.getValue();
                    put.addColumn(Bytes.toBytes(family), Bytes.toBytes(field), value.toString().getBytes());
                }
                puts.add(put);
            }
            if (puts.isEmpty()) {
                logger.warn("Puts is empty.");
                return true;
            }

            // async update
            BufferedMutatorParams params = new BufferedMutatorParams(TableName.valueOf(tableName));
            params.listener((e, m) -> {
                int numExceptions = e.getNumExceptions();
                logger.error("Failed to bulk upsert rows[{0}], caused by:\n{1}", e.getNumExceptions(), e.getMessage());
                for (int i = 0; i < numExceptions; i++) {
                    logger.error("Failed to bulk upsert row[{0}]", Bytes.toString(e.getRow(i).getRow()));
                }
            });
            mutator = this.getConnection(url).getBufferedMutator(params);
            int size = puts.size();
            mutator.mutate(puts);
            mutator.flush();
            logger.info("Success to prepare bulk upsert rows[{0}] table[{1}]", size, tableName);
            success = true;
        } catch (IOException e) {
            logger.error("Failed to bulk upsert rows table[{0}]\n", e, tableName);
        } finally {
            CloseableUtils.close(mutator);
        }
        return success;
    }

    @Override
    public boolean deleteByRowKeys(String tableName, Set<String> rowKeys) {
        return this.deleteByRowKeys(tableName, null, rowKeys);
    }

    @Override
    public boolean deleteByRowKeys(String tableName, String url, Set<String> rowKeys) {
        Table hTable = null;
        try {
            hTable = this.getConnection(url).getTable(TableName.valueOf(tableName));
            List<Delete> deleteList = new ArrayList<>();
            for (String rowKey : rowKeys) {
                Delete delete = new Delete(Bytes.toBytes(rowKey));
                deleteList.add(delete);
            }

            // delete
            if (!deleteList.isEmpty()) {
                hTable.delete(deleteList);
                logger.info("Success to delete [{0}] rows.", rowKeys.size());
            }
            hTable.close();
        } catch (Exception e) {
            logger.error("Failed to delete by rowKeys table[{0}]\n", e, tableName);
        } finally {
            CloseableUtils.close(hTable);
        }
        return false;
    }

    @Override
    public void deleteByScan(String tableName, String startRow, String stopRow) {
        Table hTable = null;
        try {
            hTable = this.getConnection().getTable(TableName.valueOf(tableName));

            Scan scan = new Scan();
            scan.setStartRow(Bytes.toBytes(startRow));
            scan.setStopRow(Bytes.toBytes(stopRow));
            scan.setCaching(2000);
            scan.setCacheBlocks(false);
            //scan.addColumn(Bytes.toBytes(Keys.OBJECTS), Bytes.toBytes(Keys.OBJECT_KEY));

            ResultScanner results = hTable.getScanner(scan);
            List<Delete> deleteList = new ArrayList<>();
            for (Result result : results) {
                Delete delete = new Delete(result.getRow());
                deleteList.add(delete);
            }
            results.close();

            // delete
            if (!deleteList.isEmpty()) {
                int size = deleteList.size();
                hTable.delete(deleteList);
                logger.info("Success to delete [{0}] rows.", size);
            }
            hTable.close();
        } catch (Exception e) {
            logger.error("Failed to delete by scan table[{0}]\n", e, tableName);
        } finally {
            CloseableUtils.close(hTable);
        }
    }

    @Override
    public List<String> listTableNames(String regex) {
        List<String> results = new ArrayList<>();
        try {
            HBaseAdmin admin = (HBaseAdmin) this.getConnection(null).getAdmin();
            TableName[] tableNames = admin.listTableNames(regex);
            for (TableName tableName : tableNames) {
                // database:collection
                results.add(tableName.getNameAsString());
            }
        } catch (Exception e) {
            logger.error(e);
        }
        return results;
    }
}
