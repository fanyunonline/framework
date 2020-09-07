package com.yyj.framework.es.search.tcp.service.impl;

import com.yyj.framework.common.util.response.PageResponse;
import com.yyj.framework.es.search.tcp.dao.SearchDao;
import com.yyj.framework.es.search.tcp.model.FilterSearchQo;
import com.yyj.framework.es.search.tcp.model.FullTextSearchQo;
import com.yyj.framework.es.search.tcp.model.IdSearchQo;
import com.yyj.framework.es.search.tcp.model.SearchQo;
import com.yyj.framework.es.search.tcp.query.FilterQuery;
import com.yyj.framework.es.search.tcp.query.FullTextQuery;
import com.yyj.framework.es.search.tcp.query.IdsQuery;
import com.yyj.framework.es.search.tcp.query.QueryParser;
import com.yyj.framework.es.search.tcp.result.SearchResult;
import com.yyj.framework.es.search.tcp.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yangyijun on 2018/11/23.
 */
@Service
public class SearchServiceImpl implements SearchService {
    @Autowired
    protected SearchDao searchDao;

    @Override
    public PageResponse filterSearch(FilterSearchQo searchQo) {
        FilterQuery filterQuery = QueryParser.toFilterQuery(searchQo);
        return buildPageResponse(searchDao.filterSearch(filterQuery), searchQo);
    }

    @Override
    public PageResponse fullTextSearch(FullTextSearchQo searchQo) {
        FullTextQuery fullTextQuery = QueryParser.toFullTextQuery(searchQo);
        return buildPageResponse(searchDao.fullTextSearch(fullTextQuery), searchQo);
    }

    @Override
    public PageResponse idSearch(IdSearchQo searchQo) {
        IdsQuery idsQuery = QueryParser.toIdsQuery(searchQo);
        return buildPageResponse(searchDao.idSearch(idsQuery), searchQo);
    }

    private PageResponse buildPageResponse(SearchResult result, SearchQo searchQo) {
        Map<String, List<Map<String, Object>>> data = new HashMap<>();
        data.put("records", result.getRecords());
        data.put("aggDatas", result.getAggDatas());
        return new PageResponse(data, result.getTotal(), searchQo.getPageNo(), searchQo.getPageSize());
    }
}
