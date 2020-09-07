package com.yyj.framework.common.core.constant;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by yangyijun on 2018/11/25.
 */
@ApiModel(value = "条件连接符(AND、OR、NOT)")
public enum LogicConnector {

    @ApiModelProperty(value = "逻辑与(AND)", example = "AND")
    AND,

    @ApiModelProperty(value = "逻辑或(OR)", example = "OR")
    OR,

    @ApiModelProperty(value = "逻辑非(NOT)", example = "NOT")
    NOT,;

}
