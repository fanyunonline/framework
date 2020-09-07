package com.yyj.framework.common.core.constant;

/**
 * Created by yangyijun on 2018/11/25.
 */
public enum FieldType {

    STRING("字符串"),
    LONG("整数"),
    DOUBLE("小数"),
    DATETIME("日期"),
    ARRAY("数组"),
    OBJECT_ARRAY("对象数组"),
    OBJECT("对象");

    private String label;

    FieldType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return this.label;
    }

    public String toString() {
        return this.name();
    }

    public boolean isNumber() {
        return equals(DOUBLE) || equals(LONG);
    }

    public boolean isText() {
        return equals(STRING) || equals(DATETIME);
    }
}
