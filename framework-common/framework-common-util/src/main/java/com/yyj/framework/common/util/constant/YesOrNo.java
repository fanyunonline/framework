package com.yyj.framework.common.util.constant;

/**
 * Created by yangyijun on 2018/11/1.
 */
public enum YesOrNo {

    YES("Y"), NO("N");

    private String code;

    YesOrNo(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
