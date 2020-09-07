package com.yyj.framework.es.admin.tcp.constant;

import com.yyj.framework.common.util.status.Status;

/**
 * Created by yangyijun on 2018/12/3.
 */
public enum EsAdminStatus implements Status {

    CREATE_INDEX_ERROR(14001, "创建index[{0}]失败"),
    CREATE_TYPE_ERROR(14002, "创建type[{0}/{1}]失败"),
    DELETE_INDEX_ERROR(14003, "删除index[{0}]失败"),
    UPSERT_DATA_ERROR(14004, "插入／更新type[{0}/{1}]数据失败"),
    DELETE_DATA_ERROR(14005, "删除type[{0}/{1}]数据失败"),;

    private int code;
    private String desc;

    EsAdminStatus(int code, String desc) {
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
