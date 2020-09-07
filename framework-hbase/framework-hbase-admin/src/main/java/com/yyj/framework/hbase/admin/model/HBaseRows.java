package com.yyj.framework.hbase.admin.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by yangyijun on 2018/3/23.
 */
@Data
public class HBaseRows {

    public static final String DEFAULT_FAMILY = "objects";
    private String family;
    private List<Map<String, Object>> rows = new ArrayList<>();

    public HBaseRows() {
        this(DEFAULT_FAMILY);
    }

    public HBaseRows(String family) {
        this.family = family;
    }


    public HBaseRows addRow(Map<String, Object> row) {
        this.rows.add(row);
        return this;
    }

    public HBaseRows addRows(List<Map<String, Object>> rows) {
        this.rows.addAll(rows);
        return this;
    }
}
