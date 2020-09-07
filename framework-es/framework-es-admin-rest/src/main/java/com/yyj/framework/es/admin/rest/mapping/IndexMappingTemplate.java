package com.yyj.framework.es.admin.rest.mapping;

import com.yyj.framework.common.util.log.Log;
import com.yyj.framework.common.util.log.LogFactory;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;

import java.io.IOException;

/**
 * Created by yangyijun on 2017/12/01.
 */
public final class IndexMappingTemplate {

    private static final Log logger = LogFactory.getLogger(IndexMappingTemplate.class);

    public static XContentBuilder defaultSettings() {
        return build(5, 1);
    }

    public static XContentBuilder buildSettings(IndexMappingQo config) {
        return build(config.getNumberOfShards(), config.getNumberOfReplicas());
    }

    ///////////////////////
    // private functions
    ///////////////////////
    public static XContentBuilder build(Integer shards, Integer replicas) {
        XContentBuilder xb = null;
        try {
            if (null == shards || null == replicas) {
                return xb;
            } else {
                xb = XContentFactory.jsonBuilder()
                        .startObject()
                            .field("index.number_of_shards", shards)
                            .field("index.number_of_replicas", replicas)
                            .startObject("analysis")
                                .startObject("analyzer")
                                    .startObject(Analyzers.IK.code())
                                        .field("type", "custom")
                                        .field("tokenizer", "ik_max_word")
                                    .endObject()
                                .endObject()
                                .startObject("normalizer")
                                    .startObject("my_normalizer")
                                        .field("type", "custom")
                                        .array("char_filter")
                                        .array("filter","lowercase", "asciifolding")//忽略大小写，对term和match生效
                                    .endObject()
                                .endObject()
                            .endObject()
                        .endObject();
            }
        } catch (IOException e) {
            logger.error(e);
        }
        return xb;
    }
}
