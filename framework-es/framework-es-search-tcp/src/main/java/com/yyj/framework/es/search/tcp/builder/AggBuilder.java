package com.yyj.framework.es.search.tcp.builder;

import com.yyj.framework.es.search.tcp.constant.AggType;
import com.yyj.framework.es.search.tcp.model.AggSearchQo;
import com.yyj.framework.es.search.tcp.query.Query;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.range.RangeAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.range.date.DateRangeAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.tophits.TopHitsAggregationBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by yangyijun on 2018/11/26.
 */
public class AggBuilder {

    private static Map<String, DateHistogramInterval> intervals = new HashMap<>();

    static {
        intervals.put("year", DateHistogramInterval.YEAR);
        intervals.put("quarter", DateHistogramInterval.QUARTER);
        intervals.put("month", DateHistogramInterval.MONTH);
        intervals.put("week", DateHistogramInterval.WEEK);
        intervals.put("day", DateHistogramInterval.DAY);
        intervals.put("hour", DateHistogramInterval.HOUR);
        intervals.put("minute", DateHistogramInterval.MINUTE);
    }

    public static void build(Query query, SearchRequestBuilder builder) {
        List<AggSearchQo> aggSearchQos = query.getAggSearchQos();
        aggSearchQos.forEach(qo -> {
            AggregationBuilder aggregationBuilder = getAggregationBuilder(qo);
            builder.addAggregation(aggregationBuilder);
            buildSubsAggs(aggregationBuilder, qo.getSubs());
        });
    }

    private static void buildSubsAggs(AggregationBuilder aggregationBuilder, List<AggSearchQo> aggSearchQos) {
        aggSearchQos.forEach(qo -> {
            AggregationBuilder subBuilder = getAggregationBuilder(qo);
            aggregationBuilder.subAggregation(subBuilder);
            buildSubsAggs(subBuilder, qo.getSubs());
        });
    }

    /**
     * Not finished yet.
     *
     * @param aggSearchQo
     * @return
     */
    private static AggregationBuilder getAggregationBuilder(AggSearchQo aggSearchQo) {
        String key = aggSearchQo.getKey();
        AggType aggType = aggSearchQo.getAggType();
        Map<String, Object> options = aggSearchQo.getOptions();
        String field = aggSearchQo.getField();
        switch (aggType) {
            case TERMS:
                return AggregationBuilders.terms(key).field(field);
            case AVG:
                return AggregationBuilders.avg(key).field(field);
            case MIN:
                return AggregationBuilders.min(key).field(field);
            case MAX:
                return AggregationBuilders.max(key).field(field);
            case SUM:
                return AggregationBuilders.sum(key).field(field);
            case VALUE_COUNT:
                return AggregationBuilders.count(key).field(field);
            case CARDINALITY:
                return AggregationBuilders.cardinality(key).field(field);
            case STATS:
                return AggregationBuilders.stats(key).field(field);
            case EXTENDED_STATS:
                return AggregationBuilders.extendedStats(key).field(field);
            case PERCENTILES:
                List<Object> percents = (List<Object>) options.get("percents");
                double[] percentiles = new double[percents.size()];
                for (int i = 0; i < percents.size(); i++) {
                    percentiles[i] = Double.valueOf(percents.get(i).toString());
                }
                return AggregationBuilders.percentiles(key).field(field).percentiles(percentiles);
            case PERCENTILE_RANKS:
                double[] values = (double[]) options.get("values");
                return AggregationBuilders.percentileRanks(key).field(field).values(values);
            case RANGE:
                return getRangeAggregationBuilder(key, options, field);
            case DATE_RANGE:
                return getDateAggregationBuilder(key, options, field);
            case DATE_HISTOGRAM:
                Object interval = options.getOrDefault("interval", "day");
                Object format = options.getOrDefault("format", "yyyy-MM-dd");
                return AggregationBuilders.dateHistogram(key).field(field)
                        .dateHistogramInterval(intervals.get(interval))
                        .format(format.toString());
            case TOP_HITS:
                return getTopHitAggregationBuilder(key, options);
            case GEO_CENTROID:
                return AggregationBuilders.geoCentroid(key).field(field);
            case GEO_BOUNDS:
                boolean wrapLongitude = (boolean) options.getOrDefault("wrapLongitude", true);
                return AggregationBuilders.geoBounds(key).field(field).wrapLongitude(wrapLongitude);
        }
        return null;
    }

    private static AggregationBuilder getTopHitAggregationBuilder(String key, Map<String, Object> fields) {
        Object size = fields.getOrDefault("size", "1");
        TopHitsAggregationBuilder topHitsAggregationBuilder = AggregationBuilders.topHits(key).size(Integer.getInteger(size.toString()));
        Object sorts = fields.get("sorts");
        if (sorts != null) {
            List<SortBuilder<?>> sortBuilders = new LinkedList<>();
            List<Map<String, Object>> sortList = (List<Map<String, Object>>) sorts;
            sortList.forEach(s -> {
                String order = s.getOrDefault("order", "ASC").toString();
                SortBuilder<?> sortBuilder = SortBuilders.fieldSort(s.get("field").toString()).order("ASC".equals(order) ? SortOrder.ASC : SortOrder.DESC);
                sortBuilders.add(sortBuilder);
            });
            topHitsAggregationBuilder.sort((SortBuilder<?>) sortBuilders);
        }
        return topHitsAggregationBuilder;
    }

    private static AggregationBuilder getDateAggregationBuilder(String key, Map<String, Object> fields, String field) {
        Object format = fields.getOrDefault("format", "yyyy-MM-dd");
        DateRangeAggregationBuilder dateRangeAggregationBuilder = AggregationBuilders.dateRange(key).field(field).format(format.toString());
        List<Map<String, Object>> dateRanges = (List<Map<String, Object>>) fields.get("ranges");
        dateRanges.forEach(r -> {
            Object from = r.get("from");
            Object to = r.get("to");
            if (null != from && null != to) {
                dateRangeAggregationBuilder.addRange((String) from, (String) to);
            } else if (null == from && null != to) {
                dateRangeAggregationBuilder.addUnboundedTo((String) to);
            } else if (null != from && null == to) {
                dateRangeAggregationBuilder.addUnboundedFrom((String) from);
            }
        });
        return dateRangeAggregationBuilder;
    }

    private static AggregationBuilder getRangeAggregationBuilder(String key, Map<String, Object> fields, String field) {
        RangeAggregationBuilder rangeAggregationBuilder = AggregationBuilders.range(key).field(field);
        List<Map<String, Object>> ranges = (List<Map<String, Object>>) fields.get("ranges");
        ranges.forEach(r -> {
            Object from = r.get("from");
            Object to = r.get("to");
            if (null != from && null != to) {
                rangeAggregationBuilder.addRange((double) from, (double) to);
            } else if (null == from && null != to) {
                rangeAggregationBuilder.addUnboundedTo((double) to);
            } else if (null != from && null == to) {
                rangeAggregationBuilder.addUnboundedFrom((double) from);
            }
        });
        return rangeAggregationBuilder;
    }
}
