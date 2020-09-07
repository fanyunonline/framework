package com.yyj.framework.es.search.tcp.builder;

import com.yyj.framework.es.search.tcp.query.Query;
import org.elasticsearch.index.query.BoolQueryBuilder;

/**
 * Created by yangyijun on 2018/11/24.
 */
public interface RootQueryBuilder {

    BoolQueryBuilder build(Query query);
}
