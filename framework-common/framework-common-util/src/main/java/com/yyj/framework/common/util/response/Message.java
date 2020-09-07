package com.yyj.framework.common.util.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by yangyijun on 2018/11/5.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "响应错误信息")
public class Message {

    @ApiModelProperty(value = "错误编码")
    private int code;

    @ApiModelProperty(value = "错误描述")
    private String desc;

}