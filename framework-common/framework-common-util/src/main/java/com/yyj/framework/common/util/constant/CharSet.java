package com.yyj.framework.common.util.constant;

/**
 * Created by yangyijun on 2018/11/7.
 */
public enum CharSet {

    UTF8("UTF-8"),
    GBK("GBK");

    private String code;

    CharSet(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
