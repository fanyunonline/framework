package com.yyj.framework.es.search.tcp.builder;

import com.yyj.framework.es.search.tcp.query.IdsQuery;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

/**
 * Created by yangyijun on 2018/11/24.
 */
public class IdsBuilder {

    public static QueryBuilder build(IdsQuery idsQuery) {
        return QueryBuilders.idsQuery(idsQuery.getTypes().toArray(new String[]{})).addIds(idsQuery.getIds().toArray(new String[]{}));
    }
}
