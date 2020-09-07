package com.yyj.framework.common.core.constant;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by yangyijun on 2018/11/25.
 */
@ApiModel(value = "条件操作符(EQ,LIKE,GT等)")
public enum Operator {

    @ApiModelProperty(value = "等于(=)", example = "EQ")
    EQ("EQ", "="),

    @ApiModelProperty(value = "不等于(!=)", example = "NOT_EQ")
    NOT_EQ("NOT_EQ", "!="),

    @ApiModelProperty(value = "模糊匹配(Like)", example = "LIKE")
    LIKE("LIKE", "LIKE"),

    @ApiModelProperty(value = "不模糊匹配(not like)", example = "NOT_LIKE")
    NOT_LIKE("NOT_LIKE", "not like"),

    @ApiModelProperty(value = "大于(>)", example = "GT")
    GT("GT", ">"),

    @ApiModelProperty(value = "大于等于(>=)", example = "GTE")
    GTE("GTE", ">="),

    @ApiModelProperty(value = "小于(<)", example = "LT")
    LT("LT", "<"),

    @ApiModelProperty(value = "小于等于(<=)", example = "LTE")
    LTE("LTE", "<="),

    @ApiModelProperty(value = "属于(in)", example = "IN")
    IN("IN", "IN"),

    @ApiModelProperty(value = "不属于(not in)", example = "NOT_IN")
    NOT_IN("NOT_IN", "not in"),

    @ApiModelProperty(value = "是否为空(is null)", example = "ISNULL")
    IS_NULL("ISNULL", "is null"),

    @ApiModelProperty(value = "是否不为空(is not null)", example = "IS_NOT_NULL")
    IS_NOT_NULL("IS_NOT_NULL", "is not null"),

    @ApiModelProperty(value = "在A和B之间(between)", example = "BETWEEN")
    BETWEEN("BETWEEN", "between"),

    @ApiModelProperty(value = "不在A和B之间(not between)", example = "NOT BETWEEN")
    NOT_BETWEEN("NOT_BETWEEN", "not between"),

    @ApiModelProperty(value = "成员属于，eg: {\"level\":\"level1\",\"name\":\"社区1\"} in communities", example = "IN")
    IN_ARRAY_FIELD("IN_ARRAY_FIELD", "in"),;

    private String value;
    private String desc;

    Operator(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public String getValue() {
        return this.value;
    }

    public static Operator byValue(String value) {
        for (Operator operator : Operator.values()) {
            if (StringUtils.equalsIgnoreCase(value, operator.getValue())) {
                return operator;
            }
        }
        return null;
    }
}