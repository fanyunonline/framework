package com.yyj.framework.es.search.tcp.query;

import com.yyj.framework.es.search.tcp.model.FullTextSearchQo;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by yangyijun on 2018/11/23.
 */
@Data
public class FullTextQuery extends Query {
    private String keyword;
    private Set<FullTextSearchQo.MatchField> matchFields = new HashSet<>();
}
