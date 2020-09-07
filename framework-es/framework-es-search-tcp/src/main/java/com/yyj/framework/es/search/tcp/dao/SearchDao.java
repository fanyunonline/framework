package com.yyj.framework.es.search.tcp.dao;


import com.yyj.framework.es.search.tcp.query.FilterQuery;
import com.yyj.framework.es.search.tcp.query.FullTextQuery;
import com.yyj.framework.es.search.tcp.query.IdsQuery;
import com.yyj.framework.es.search.tcp.result.SearchResult;

/**
 * Created by yangyijun on 2018/11/23.
 */
public interface SearchDao {

    SearchResult filterSearch(FilterQuery filterQuery);

    SearchResult fullTextSearch(FullTextQuery fullTextQuery);

    SearchResult idSearch(IdsQuery idsQuery);

}
