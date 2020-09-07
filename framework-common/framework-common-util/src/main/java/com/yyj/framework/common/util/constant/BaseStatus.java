package com.yyj.framework.common.util.constant;

import com.yyj.framework.common.util.status.Status;

/**
 * Created by yangyijun on 2018/11/5.
 */
public enum BaseStatus implements Status {

    UNKOWN_ERROR(-1, "未知错误");

    private int code;
    private String desc;

    BaseStatus(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getDesc() {
        return desc;
    }
}
