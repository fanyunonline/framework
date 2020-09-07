package com.yyj.framework.es.search.tcp.service;

import com.yyj.framework.common.util.response.PageResponse;
import com.yyj.framework.es.search.tcp.model.FilterSearchQo;
import com.yyj.framework.es.search.tcp.model.FullTextSearchQo;
import com.yyj.framework.es.search.tcp.model.IdSearchQo;

/**
 * Created by yangyijun on 2018/11/23.
 */
public interface SearchService {
    PageResponse filterSearch(FilterSearchQo esSearchQo);

    PageResponse fullTextSearch(FullTextSearchQo esSearchQo);

    PageResponse idSearch(IdSearchQo esSearchQo);
}
