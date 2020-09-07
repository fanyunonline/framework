package com.yyj.framework.es.search.tcp.query;

import com.yyj.framework.common.util.sort.Sort;
import com.yyj.framework.es.search.tcp.model.AggSearchQo;
import lombok.Data;

import java.util.*;

/**
 * Created by yangyijun on 2018/11/23.
 */
@Data
public class Query {

    /* Query */
    private Set<String> indexs = new HashSet<>();
    private Set<String> types = new HashSet<>();
    private List<AggSearchQo> aggSearchQos = new LinkedList<>();
    private List<Sort> sorts = new ArrayList<>();

    /* Result */
    private int pageNo = 1;
    private int pageSize = 10;
    private boolean highlight = false;
    public static String HIGHLIGHTER_PRE_TAGS = "<em>";
    public static String HIGHLIGHTER_POST_TAGS = "</em>";
    private String highlighterPreTags = HIGHLIGHTER_PRE_TAGS;
    private String highlighterPostTags = HIGHLIGHTER_POST_TAGS;
    private Set<String> hitFields = new HashSet<>();
    private Set<String> highlightFields = new HashSet<>();

    public Query() {
    }

    public Query(Set<String> indexs) {
        this.indexs = indexs;
    }

    public Query(Set<String> indexs, String type) {
        this.indexs = indexs;
        this.types.add(type);
    }


    public int getFrom() {
        return (pageNo - 1) * pageSize;
    }

    public int getSize() {
        return pageSize;
    }


}
