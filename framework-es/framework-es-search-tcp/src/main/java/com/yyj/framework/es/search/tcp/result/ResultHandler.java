package com.yyj.framework.es.search.tcp.result;

import com.yyj.framework.common.util.log.Log;
import com.yyj.framework.common.util.log.LogFactory;
import com.yyj.framework.es.search.tcp.bean.Fields;
import com.yyj.framework.es.search.tcp.query.Query;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.elasticsearch.search.aggregations.bucket.range.Range;
import org.elasticsearch.search.aggregations.bucket.range.date.InternalDateRange;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.metrics.NumericMetricsAggregation;
import org.elasticsearch.search.aggregations.metrics.geobounds.GeoBounds;
import org.elasticsearch.search.aggregations.metrics.geocentroid.GeoCentroid;
import org.elasticsearch.search.aggregations.metrics.percentiles.PercentileRanks;
import org.elasticsearch.search.aggregations.metrics.percentiles.Percentiles;
import org.elasticsearch.search.aggregations.metrics.stats.Stats;
import org.elasticsearch.search.aggregations.metrics.stats.extended.ExtendedStats;
import org.elasticsearch.search.aggregations.metrics.tophits.TopHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;

import java.util.*;

/**
 * Created by yangyijun on 2018/11/23.
 */
public class ResultHandler {

    private static final Log logger = LogFactory.getLogger(ResultHandler.class);

    public static SearchResult getSearchResult(Query query, SearchResponse searchResponse) {
        SearchResult result = new SearchResult();
        List<Map<String, Object>> resultList = new ArrayList<>();
        boolean isHighlight = query.isHighlight();
        SearchHits searchHits = searchResponse.getHits();
        for (SearchHit hit : searchHits) {
            Map<String, Object> row = hit.getSourceAsMap();
            row.put(Fields.INDEX, hit.getIndex());
            row.put(Fields.SCHEMA, hit.getType());
            row.put(Fields.ID, hit.getId());
            row.put(Fields.SCORE, hit.getScore());
            resultList.add(row);

            if (!isHighlight) {
                continue;
            }
            buildHighlightField(hit, row);
        }
        result.setRecords(resultList);
        result.setTotal(searchHits.getTotalHits());
        result.setAggDatas(handleAggsResult(searchResponse));
        return result;
    }

    ///////////////////////
    // private functions
    ///////////////////////
    private static List<Map<String, Object>> handleAggsResult(SearchResponse searchResponse) {
        List<Map<String, Object>> aggDatas = new LinkedList<>();
        Aggregations aggregations = searchResponse.getAggregations();
        if (null == aggregations) {
            return aggDatas;
        }
        Map<String, Aggregation> rootAggs = aggregations.asMap();
        for (Map.Entry<String, Aggregation> kv : rootAggs.entrySet()) {
            Map<String, Object> aggData = new LinkedHashMap<>();
            buildAggResult(kv.getValue(), aggDatas, aggData);
        }
        return aggDatas;
    }

    private static void buildAggResult(Aggregation agg, List<Map<String, Object>> aggDatas, Map<String, Object> aggData) {
        if (agg instanceof StringTerms) {
            buildTermResult((StringTerms) agg, aggDatas);
        } else if (agg instanceof NumericMetricsAggregation.SingleValue) {
            aggData.put(agg.getName(), ((NumericMetricsAggregation.SingleValue) agg).getValueAsString());
        } else if (agg instanceof Stats) {
            buildStatsResult(agg, aggData);
        } else if (agg instanceof ExtendedStats) {
            buildEextendedStatsResult(agg, aggData);
        } else if (agg instanceof Percentiles) {
            buildPercentilesResult(agg, aggData);
        } else if (agg instanceof PercentileRanks) {
            buildPercentileRanksResult(agg, aggData);
        } else if (agg instanceof Range) {
            buildRangeResult(agg, aggData);
        } else if (agg instanceof InternalDateRange) {
            buildDateRangeResult((InternalDateRange) agg);
        } else if (agg instanceof Histogram) {
            buildHistogramResult(agg, aggData);
        } else if (agg instanceof TopHits) {

        } else if (agg instanceof GeoCentroid) {
            buildGeoCentroidResult(agg, aggData);
        } else if (agg instanceof GeoBounds) {
            buildGeoBoundsResult(agg, aggData);
        }
        if (!aggData.isEmpty()) {
            aggDatas.add(aggData);
        }
    }

    private static void buildDateRangeResult(InternalDateRange agg) {
        InternalDateRange dateRange = agg;
        dateRange.getBuckets().forEach(d -> {
        });
    }

    private static void buildPercentileRanksResult(Aggregation agg, Map<String, Object> aggData) {
        PercentileRanks percentileRanks = (PercentileRanks) agg;
        Map<String, Object> percentileRanksData = new LinkedHashMap<>();
        percentileRanks.forEach(f -> percentileRanksData.put(String.valueOf(f.getPercent()), f.getValue()));
        aggData.put(agg.getName(), percentileRanksData);
    }

    private static void buildPercentilesResult(Aggregation agg, Map<String, Object> aggData) {
        Percentiles percentiles = (Percentiles) agg;
        Map<String, Object> percentilesData = new LinkedHashMap<>();
        percentiles.forEach(f -> percentilesData.put(String.valueOf(f.getPercent()), f.getValue()));
        aggData.put(agg.getName(), percentilesData);
    }

    private static void buildGeoBoundsResult(Aggregation agg, Map<String, Object> aggData) {
        GeoBounds geoBounds = (GeoBounds) agg;
        double bottomRightLat = geoBounds.bottomRight().getLat();
        double bottomRightLon = geoBounds.bottomRight().getLon();
        double topLeftLat = geoBounds.topLeft().getLat();
        double topLeftLon = geoBounds.topLeft().getLon();
        Map<String, Object> geoBoundsData = new LinkedHashMap<>();

        Map<String, Object> bottomRight = new LinkedHashMap<>();
        bottomRight.put("lon", bottomRightLon);
        bottomRight.put("lat", bottomRightLat);
        geoBoundsData.put("bottomRight", bottomRight);

        Map<String, Object> topLeft = new LinkedHashMap<>();
        topLeft.put("lon", topLeftLon);
        topLeft.put("lat", topLeftLat);
        geoBoundsData.put("topLeft", topLeft);

        aggData.put(agg.getName(), geoBoundsData);
    }

    private static void buildGeoCentroidResult(Aggregation agg, Map<String, Object> aggData) {
        GeoCentroid geoCentroid = (GeoCentroid) agg;
        double lat = geoCentroid.centroid().getLat();
        double lon = geoCentroid.centroid().getLon();
        Map<String, Object> geoCentroidData = new LinkedHashMap<>();
        geoCentroidData.put("lat", lat);
        geoCentroidData.put("lon", lon);
        aggData.put(agg.getName(), geoCentroidData);
    }

    private static void buildHistogramResult(Aggregation agg, Map<String, Object> aggData) {
        Histogram histogram = (Histogram) agg;
        List<Map<String, Object>> histogramDatas = new LinkedList<>();
        histogram.getBuckets().forEach(h -> {
            Map<String, Object> histogramData = new LinkedHashMap<>();
            histogramData.put("doc_count", h.getDocCount());
            histogramData.put("key_as_string", h.getKeyAsString());
            Map<String, Aggregation> subAggs = h.getAggregations().asMap();
            for (Map.Entry<String, Aggregation> kv : subAggs.entrySet()) {
                buildAggResult(kv.getValue(), histogramDatas, histogramData);
            }
        });
        aggData.put(agg.getName(), histogramDatas);
    }

    private static void buildRangeResult(Aggregation agg, Map<String, Object> aggData) {
        Range range = (Range) agg;
        List<Map<String, Object>> rangeDatas = new LinkedList<>();
        range.getBuckets().forEach(r -> {
            Map<String, Object> rangeData = new LinkedHashMap<>();
            rangeData.put("from", r.getFrom());
            rangeData.put("to", r.getTo());
            rangeData.put("doc_count", r.getDocCount());
            rangeDatas.add(rangeData);
        });
        aggData.put(agg.getName(), rangeDatas);
    }

    private static void buildEextendedStatsResult(Aggregation agg, Map<String, Object> aggData) {
        ExtendedStats extendedStats = (ExtendedStats) agg;
        Map<String, Object> extendedStatsData = new LinkedHashMap<>();
        extendedStatsData.put("count", extendedStats.getCount());
        extendedStatsData.put("max", extendedStats.getMax());
        extendedStatsData.put("min", extendedStats.getMin());
        extendedStatsData.put("avg", extendedStats.getAvg());
        extendedStatsData.put("sum", extendedStats.getSum());
        extendedStatsData.put("sum_of_squares", extendedStats.getSumOfSquares());
        extendedStatsData.put("variance", extendedStats.getVariance());
        extendedStatsData.put("std_deviation", extendedStats.getStdDeviation());

        double lower = extendedStats.getStdDeviationBound(ExtendedStats.Bounds.LOWER);
        double upper = extendedStats.getStdDeviationBound(ExtendedStats.Bounds.UPPER);
        Map<String, Object> boundsData = new LinkedHashMap<>();
        boundsData.put("lower", lower);
        boundsData.put("upper", upper);
        extendedStatsData.put("std_deviation_bounds", boundsData);

        aggData.put(agg.getName(), extendedStatsData);
    }

    private static void buildStatsResult(Aggregation agg, Map<String, Object> aggData) {
        Stats stats = (Stats) agg;
        Map<String, Object> statsData = new LinkedHashMap<>();
        statsData.put("count", stats.getCount());
        statsData.put("max", stats.getMax());
        statsData.put("min", stats.getMin());
        statsData.put("avg", stats.getAvg());
        statsData.put("sum", stats.getSum());
        aggData.put(agg.getName(), statsData);
    }

    private static void buildTermResult(StringTerms agg, List<Map<String, Object>> aggDatas) {
        StringTerms terms = agg;
        List<StringTerms.Bucket> buckets = terms.getBuckets();
        buckets.forEach(bucket -> {
            Map<String, Object> subAggData = new LinkedHashMap<>();
            subAggData.put(bucket.getKey().toString(), bucket.getDocCount());
            Map<String, Aggregation> subAggs = bucket.getAggregations().asMap();
            for (Map.Entry<String, Aggregation> kv : subAggs.entrySet()) {
                buildAggResult(kv.getValue(), aggDatas, subAggData);
            }
        });
    }

    private static void buildHighlightField(SearchHit hit, Map<String, Object> row) {
        Map<String, HighlightField> hiMap = hit.getHighlightFields();
        Iterator<Map.Entry<String, HighlightField>> hiIterator = hiMap.entrySet().iterator();
        while (hiIterator.hasNext()) {
            Map.Entry<String, HighlightField> entry = hiIterator.next();
            Object[] contents = entry.getValue().fragments();
            if (contents.length == 1) {
                row.put(entry.getKey(), contents[0].toString());
            } else {
                logger.warn("The results of the highlight in the search results appear more data, fragments size = " + contents.length);
            }
        }
    }
}
