package com.yyj.framework.es.search.tcp.query;


import com.yyj.framework.es.search.tcp.model.FilterSearchQo;
import com.yyj.framework.es.search.tcp.model.FullTextSearchQo;
import com.yyj.framework.es.search.tcp.model.IdSearchQo;
import com.yyj.framework.es.search.tcp.model.SearchQo;

/**
 * Created by yangyijun on 2018/11/23.
 */
public class QueryParser {

    public static FilterQuery toFilterQuery(FilterSearchQo filterSearchQo) {
        FilterQuery filterQuery = new FilterQuery();
        buildQuery(filterSearchQo, filterQuery);
        filterQuery.setMust(filterSearchQo.getMust());
        filterQuery.setShould(filterSearchQo.getShould());
        filterQuery.setMustNot(filterSearchQo.getMustNot());
        return filterQuery;
    }

    public static FullTextQuery toFullTextQuery(FullTextSearchQo fullTextSearchQo) {
        FullTextQuery fullTextQuery = new FullTextQuery();
        buildQuery(fullTextSearchQo, fullTextQuery);
        fullTextQuery.setKeyword(fullTextSearchQo.getKeyword());
        fullTextQuery.setMatchFields(fullTextSearchQo.getMatchFields());
        return fullTextQuery;
    }

    public static IdsQuery toIdsQuery(IdSearchQo idSearchQo) {
        IdsQuery idsQuery = new IdsQuery();
        buildQuery(idSearchQo, idsQuery);
        idsQuery.setIds(idSearchQo.getIds());
        return idsQuery;
    }

    ///////////////////////
    // private functions
    ///////////////////////
    private static void buildQuery(SearchQo searchQo, Query query) {
        query.setIndexs(searchQo.getIndexs());
        query.setTypes(searchQo.getTypes());
        query.setAggSearchQos(searchQo.getAggSearchQos());
        query.setPageNo(searchQo.getPageNo());
        query.setPageSize(searchQo.getPageSize());
        query.setHighlightFields(searchQo.getHighlightFields());
        query.setHitFields(searchQo.getHitFields());
        query.setSorts(searchQo.getSorts());
    }
}
