package com.yyj.framework.es.admin.tcp.mapping;

import com.yyj.framework.common.util.log.Log;
import com.yyj.framework.common.util.log.LogFactory;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.Map;

/**
 * Created by yangyijun on 2017/12/01.
 */
public final class TypeMappingTemplate {

    private static final Log logger = LogFactory.getLogger(TypeMappingTemplate.class);

    public static XContentBuilder defaultSettings() {
        XContentBuilder xb = null;
        try {
            xb = XContentFactory.jsonBuilder()
                    .startObject()
                        .startObject("_all")
                            .field("enabled", false)
                        .endObject()
                        .field("date_detection", true)
                        .array("dynamic_date_formats", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd")
                        .startArray("dynamic_templates")
                            .startObject()
                                .startObject("strings")
                                     .field("match_mapping_type", "string")
                                     .startObject("mapping")
                                            .field("type", "text")
                                            .field("analyzer", Analyzers.IK.code())
                                            .startObject("fields")
                                                .startObject("keyword")
                                                    .field("type", "keyword")
                                                    .field("normalizer", "my_normalizer")
                                                .endObject()
                                            .endObject()
                                     .endObject()
                                .endObject()
                            .endObject()
                        .endArray()
                    .endObject();
        } catch (IOException e) {
            logger.error(e);
        }
        return xb;
    }

    public static XContentBuilder buildSettings(Map<String, Field> properties) {
        XContentBuilder builder = null;
        try {
            if (CollectionUtils.isEmpty(properties)) {
                return builder;
            } else {
                builder = XContentFactory.jsonBuilder();
                builder.startObject();
                builder.startObject("properties");
                for (Field f : properties.values()) {
                    builder.startObject(f.getName());

                    // children-> has properties
                    if (f.hasProperties()) {
                        builder.startObject("properties");
                        for (Field pf : f.getProperties().values()) {
                            builder.startObject(pf.getName());
                            builder.field("type", pf.getType().code());
                            if (StringUtils.isNotBlank(f.getAnalyzer())) {
                                builder.field("analyzer", f.getAnalyzer());
                            }
                            builder.endObject();
                        }
                        builder.endObject();
                    } else {
                        DataType fieldType = f.getType();
                        builder.field("type", fieldType.code());
                        if (DataType.STRING == fieldType || DataType.KEYWORD == fieldType) {
                            builder.startObject("fields");
                            builder.startObject("keyword");
                            builder.field("type", "keyword");
                            builder.field("normalizer", "my_normalizer");
                            builder.endObject();
                            builder.endObject();
                        }

                        if (StringUtils.isNotBlank(f.getAnalyzer())) {
                            builder.field("analyzer", f.getAnalyzer());
                        }
                    }
                    builder.endObject();
                }
                builder.endObject();
                builder.endObject();
            }
        } catch (IOException e) {
            logger.error(e);
        }
        return builder;
    }
}
