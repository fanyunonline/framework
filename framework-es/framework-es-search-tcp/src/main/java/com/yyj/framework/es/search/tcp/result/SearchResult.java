package com.yyj.framework.es.search.tcp.result;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * Created by yangyijun on 2018/11/23.
 */
@Data
public class SearchResult {
    private long total;
    private List<Map<String, Object>> records;
    private List<Map<String, Object>> aggDatas;
}
