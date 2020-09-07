package com.yyj.framework.es.search.tcp.query;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by yangyijun on 2018/11/23.
 */
@Data
public class IdsQuery extends Query {
    private Set<String> ids = new HashSet<>();
}
