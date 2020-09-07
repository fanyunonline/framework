package com.yyj.framework.es.admin.tcp.mapping;

import lombok.Data;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by yangyijun on 2018/12/8.
 */
@Data
public class Field implements Serializable {

    private String name;
    private DataType type;
    private String analyzer;
    private String searchAnalyzer;
    private Map<String, Field> properties = new LinkedHashMap<>();
    private Map<String, Field> fields = new LinkedHashMap<>();


    public Field(String name, DataType type) {
        this.name = name;
        this.type = type;
        this.initializeAnalyzer();
    }

    public void addProperty(String field, DataType type) {
        this.properties.put(field, new Field(field, type));
    }

    public boolean hasProperties() {
        return !properties.isEmpty();
    }


    private void initializeAnalyzer() {
        if (DataType.STRING == type) {
            this.analyzer = Analyzers.IK.code();
        }
    }
}