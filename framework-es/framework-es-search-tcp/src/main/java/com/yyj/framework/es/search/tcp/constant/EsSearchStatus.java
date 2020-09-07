package com.yyj.framework.es.search.tcp.constant;

import com.yyj.framework.common.util.status.Status;

/**
 * Created by yangyijun on 2018/12/4.
 */
public enum EsSearchStatus implements Status {
    BUILD_FILTER_ERROR(15001, "构建filter条件失败"),;

    private int code;
    private String desc;

    EsSearchStatus(int code, String desc) {
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
