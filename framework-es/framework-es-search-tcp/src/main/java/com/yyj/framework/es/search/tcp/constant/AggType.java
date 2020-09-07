package com.yyj.framework.es.search.tcp.constant;

/**
 * Created by yangyijun on 2018/11/25.
 */
public enum AggType {
    TERMS("terms", "分组"),
    AVG("avg", "平均值"),
    MIN("min", "最小值"),
    MAX("max", "最大值"),
    SUM("sum", "求和"),
    VALUE_COUNT("value_count", "存在指定字段记录汇总"),
    CARDINALITY("cardinality", "去重汇总"),
    STATS("stats", "统计count max min avg sum"),
    EXTENDED_STATS("extended_stats", "比stats多4个统计结果： 平方和、方差、标准差、平均值加/减两个标准差的区间"),
    PERCENTILES("percentiles", "对指定字段的值按从小到大累计每个值对应的文档数的占比"),
    PERCENTILE_RANKS("percentile_ranks", "统计值小于等于指定值的文档占比"),
    RANGE("range", "范围分组聚合"),
    DATE_RANGE("date_range", "日期范围聚合"),
    DATE_HISTOGRAM("date_histogram", "时间直方图（柱状）聚合;year (1y), quarter (1q), month (1M), week (1w), day (1d), hour (1h), minute (1m), second (1s)"),
    TOP_HITS("top_hits", "分组取topn"),
    GEO_CENTROID("geo_centroid", "计算中心点"),
    GEO_BOUNDS("geo_bounds", "地理坐标矩形区域"),;

    private String type;
    private String desc;

    AggType(String type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
