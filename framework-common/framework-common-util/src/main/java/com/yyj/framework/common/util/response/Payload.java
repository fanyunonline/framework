package com.yyj.framework.common.util.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yangyijun
 * @description Response响应数据
 * @date 2018/10/16
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "非分页响应数据")
public class Payload<T> {

    @ApiModelProperty(value = "业务数据内容")
    private T data;
}
