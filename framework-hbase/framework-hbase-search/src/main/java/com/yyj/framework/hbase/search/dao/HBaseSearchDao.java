package com.yyj.framework.hbase.search.dao;

import com.yyj.framework.hbase.search.model.HBaseSearchQo;

import java.util.List;
import java.util.Map;

/**
 * Created by yangyijun on 2018/2/5.
 */
public interface HBaseSearchDao {

    /**
     * Scan data with rowKeys.
     *
     * @return
     */
    List<Map<String, String>> getByRowKeys(HBaseSearchQo hBaseSearchQo);

}
