package com.yyj.framework.hbase.search.service;


import com.yyj.framework.common.util.response.Response;
import com.yyj.framework.hbase.search.model.HBaseSearchQo;


/**
 * Created by yangyijun on 2018/11/12.
 */
public interface HBaseSearchService {
    Response search(HBaseSearchQo hBaseSearchQo);
}
