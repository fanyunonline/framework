package com.yyj.framework.es.search.tcp.dao.impl;

import com.alibaba.fastjson.JSON;
import com.yyj.framework.common.util.log.Log;
import com.yyj.framework.common.util.log.LogFactory;
import com.yyj.framework.common.util.sort.Direction;
import com.yyj.framework.common.util.sort.Sort;
import com.yyj.framework.es.core.client.tcp.EsTcpClient;
import com.yyj.framework.es.search.tcp.builder.*;
import com.yyj.framework.es.search.tcp.dao.SearchDao;
import com.yyj.framework.es.search.tcp.query.FilterQuery;
import com.yyj.framework.es.search.tcp.query.FullTextQuery;
import com.yyj.framework.es.search.tcp.query.IdsQuery;
import com.yyj.framework.es.search.tcp.query.Query;
import com.yyj.framework.es.search.tcp.result.ResultHandler;
import com.yyj.framework.es.search.tcp.result.SearchResult;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Set;


/**
 * Created by yangyijun on 2018/11/23.
 */
@Repository
public class SearchDaoImpl implements SearchDao {

    private static final Log logger = LogFactory.getLogger(SearchDaoImpl.class);

    @Autowired
    private EsTcpClient esClient;

    @Value("${es.print.search.statement.log:false}")
    private boolean printSearchStatement;

    private TransportClient getClient() {
        try {
            return esClient.getClient();
        } catch (Exception e) {
            logger.error("create elasticsearch client exception;", e);
        }
        return null;
    }

    @Override
    public SearchResult filterSearch(FilterQuery filterQuery) {
        return buildSearchResult(filterQuery, createSearchBuilder(filterQuery, new FilterQueryBuilder()));
    }

    @Override
    public SearchResult fullTextSearch(FullTextQuery fullTextQuery) {
        return buildSearchResult(fullTextQuery, createSearchBuilder(fullTextQuery, new FullTextQueryBuilder()));

    }

    @Override
    public SearchResult idSearch(IdsQuery idsQuery) {
        return buildSearchResult(idsQuery, createIdSearchRequestBuilder(idsQuery));
    }


    ///////////////////////
    // private functions
    ///////////////////////
    private SearchRequestBuilder createSearchBuilder(Query query, RootQueryBuilder queryBuilder) {
        SearchRequestBuilder builder = this.getClient()
                .prepareSearch(query.getIndexs().toArray(new String[]{}))
                .setTypes(query.getTypes().toArray(new String[]{}))
                .setQuery(queryBuilder.build(query))
                .setFetchSource(true)
                .setFrom(query.getFrom())
                .setSize(query.getSize());
        //build aggregation
        AggBuilder.build(query, builder);
        buildSortHighlightHit(query, builder);
        return builder;
    }

    private SearchRequestBuilder createIdSearchRequestBuilder(IdsQuery idsQuery) {
        SearchRequestBuilder searchBuilder = this.getClient()
                .prepareSearch(idsQuery.getIndexs().toArray(new String[]{}))
                .setQuery(IdsBuilder.build(idsQuery))
                .setFetchSource(true)
                .setSize(idsQuery.getIds().size());
        //build aggregation
        AggBuilder.build(idsQuery, searchBuilder);
        addHitFields(idsQuery, searchBuilder);
        return searchBuilder;
    }

    private void buildSortHighlightHit(Query query, SearchRequestBuilder searchBuilder) {
        // sort fields
        addSortFields(query, searchBuilder);
        // highlight fields
        addHighlightFields(query, searchBuilder);
        // hit fields
        addHitFields(query, searchBuilder);
    }


    private void addSortFields(Query query, SearchRequestBuilder searchBuilder) {
        List<Sort> sorts = query.getSorts();
        if (CollectionUtils.isEmpty(sorts)) {
            return;
        }
        sorts.forEach(sort -> {
            SortOrder sortOrder = sort.getDirection() == Direction.ASC ? SortOrder.ASC : SortOrder.DESC;
            String property = sort.getProperty();
            searchBuilder.addSort(property, sortOrder);
//            if (property.endsWith(".keyword")) {
//                searchBuilder.addSort(property, sortOrder);
//            } else {
//                searchBuilder.addSort(property + ".keyword", sortOrder);
//            }
        });
    }

    private void addHighlightFields(Query query, SearchRequestBuilder searchBuilder) {
        Set<String> highlightFields = query.getHighlightFields();
        if (CollectionUtils.isEmpty(highlightFields)) {
            return;
        }
        query.setHighlight(true);
        HighlightBuilder hiBuilder = new HighlightBuilder();
        hiBuilder.preTags(query.getHighlighterPreTags());
        hiBuilder.postTags(query.getHighlighterPostTags());
        highlightFields.forEach(field -> hiBuilder.field(field));
        searchBuilder.highlighter(hiBuilder);
    }

    private void addHitFields(Query query, SearchRequestBuilder searchBuilder) {
        Set<String> hitFields = query.getHitFields();
        if (CollectionUtils.isEmpty(hitFields)) {
            return;
        }
        searchBuilder.setFetchSource(hitFields.toArray(new String[]{}), null);
    }

    private SearchResult buildSearchResult(Query query, SearchRequestBuilder searchBuilder) {
        String queryDSL = "";
        try {
            queryDSL = searchBuilder.toString();
            if (printSearchStatement) {
                logger.info("Index:{0},Type:{1} Query DSL:\n{2}", query.getIndexs(), JSON.toJSONString(query.getTypes()), queryDSL);
            }
            SearchResponse searchResponse = searchBuilder.get();
            return ResultHandler.getSearchResult(query, searchResponse);
        } catch (Exception e) {
            logger.error("[{0}/{1}] search error, while query DSL:\n{2}\n", e, query.getIndexs(), query.getTypes(), queryDSL);
            throw e;
        }
    }
}
