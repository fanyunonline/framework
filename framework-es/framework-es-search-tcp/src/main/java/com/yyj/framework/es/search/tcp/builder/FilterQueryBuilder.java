package com.yyj.framework.es.search.tcp.builder;

import com.yyj.framework.common.core.constant.Operator;
import com.yyj.framework.common.util.json.JsonUtils;
import com.yyj.framework.es.search.tcp.model.EsSearchFilter;
import com.yyj.framework.es.search.tcp.query.FilterQuery;
import com.yyj.framework.es.search.tcp.query.Query;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.util.CollectionUtils;

import java.util.List;

import static com.yyj.framework.common.util.json.JsonUtils.toArray;
import static org.elasticsearch.index.query.QueryBuilders.boolQuery;


/**
 * Created by yangyijun on 2018/11/25.
 */
public class FilterQueryBuilder implements RootQueryBuilder {
    private static final String WILDCARD = "*";

    @Override
    public BoolQueryBuilder build(Query query) {
        FilterQuery filterQuery = (FilterQuery) query;

        List<EsSearchFilter> must = filterQuery.getMust();
        List<EsSearchFilter> should = filterQuery.getShould();
        List<EsSearchFilter> mustNot = filterQuery.getMustNot();

        BoolQueryBuilder mustBuilder = boolQuery();
        buildMust(must, mustBuilder);
        buildShould(should, mustBuilder);
        buildMustNot(mustNot, mustBuilder);

        return mustBuilder;
    }

    ///////////////////////
    // private functions
    ///////////////////////
    private void buildMustNot(List<EsSearchFilter> mustNot, BoolQueryBuilder mustBuilder) {
        if (!CollectionUtils.isEmpty(mustNot)) {
            BoolQueryBuilder boolQBuilder = boolQuery();
            mustNot.forEach(esSearchFilter -> {
                QueryBuilder queryBuilder = getBuilder(esSearchFilter);
                if (null != queryBuilder) {
                    boolQBuilder.mustNot(queryBuilder);
                }
            });
            mustBuilder.must(boolQBuilder);
        }
    }

    private void buildShould(List<EsSearchFilter> should, BoolQueryBuilder mustBuilder) {
        if (!CollectionUtils.isEmpty(should)) {
            BoolQueryBuilder boolQBuilder = boolQuery();
            should.forEach(esSearchFilter -> {
                QueryBuilder queryBuilder = getBuilder(esSearchFilter);
                if (null != queryBuilder) {
                    boolQBuilder.should(queryBuilder);
                }
            });
            mustBuilder.must(boolQBuilder);
        }
    }

    private void buildMust(List<EsSearchFilter> must, BoolQueryBuilder mustBuilder) {
        if (!CollectionUtils.isEmpty(must)) {
            must.forEach(esSearchFilter -> {
                QueryBuilder queryBuilder = getBuilder(esSearchFilter);
                if (null != queryBuilder) {
                    mustBuilder.must(queryBuilder);
                }
            });
        }
    }

    private static QueryBuilder getBuilder(EsSearchFilter esSearchFilter) {
        Object value = esSearchFilter.getValue();
        String field = handleField(esSearchFilter);
        float boost = esSearchFilter.getBoost();
        switch (esSearchFilter.getOperator()) {
            case EQ:
                return QueryBuilders.termQuery(field, value).boost(boost);
            case NOT_EQ:
                return boolQuery().mustNot(QueryBuilders.termQuery(field, value)).boost(boost);
            case GT:
                return QueryBuilders.rangeQuery(field).gt(value).boost(boost);
            case GTE:
                return QueryBuilders.rangeQuery(field).gte(value).boost(boost);
            case LT:
                return QueryBuilders.rangeQuery(field).lt(value).boost(boost);
            case LTE:
                return QueryBuilders.rangeQuery(field).lte(value).boost(boost);
            case LIKE:
                return QueryBuilders.wildcardQuery(field, WILDCARD + value + WILDCARD).boost(boost);
            case IN:
                return QueryBuilders.termsQuery(field, toArray(value.toString(), String.class)).boost(boost);
            case NOT_IN:
                return QueryBuilders.boolQuery().mustNot(QueryBuilders.termsQuery(field, toArray(value.toString(), String.class))).boost(boost);
            case BETWEEN:
                String[] values = JsonUtils.toArray(value.toString(), String.class);
                return QueryBuilders.rangeQuery(field).from(values[0], true).to(values[1], true).boost(boost);
        }
        return null;
    }

    /**
     * 模糊匹配字段、数值类型字段不加.keyword
     */
    private static String handleField(EsSearchFilter esSearchFilter) {
        String field = esSearchFilter.getField();
        if (Operator.LIKE.equals(esSearchFilter.getOperator()) || esSearchFilter.getFieldType().isNumber()) {
            if (field.endsWith(".keyword")) {
                return field.substring(0, field.indexOf("."));
            }
        } else if (!field.endsWith(".keyword")) {
            return String.format("%s.keyword", field);
        }
        return field;
    }

}