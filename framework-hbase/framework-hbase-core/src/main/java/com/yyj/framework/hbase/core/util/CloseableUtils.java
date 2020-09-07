package com.yyj.framework.hbase.core.util;

import org.apache.hadoop.hbase.client.BufferedMutator;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.Table;

import java.io.IOException;

/**
 * Created by yangyijun on 2018/3/23.
 */
public class CloseableUtils {

    public static void close(Table table) {
        if (table == null) {
            return;
        }
        try {
            table.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void close(HBaseAdmin admin) {
        if (admin == null) {
            return;
        }
        try {
            admin.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void close(BufferedMutator mutator) {
        if (mutator == null) {
            return;
        }
        try {
            mutator.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
