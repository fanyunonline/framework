package com.yyj.framework.common.util.constant;

/**
 * Created by yangyijun on 2018/11/1.
 */
public enum CrudType {

    C("create"), R("retrive"), U("update"), D("delete");

    private String code;

    CrudType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
