package com.yyj.framework.common.core.model;

import com.yyj.framework.common.core.constant.Operator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Created by yangyijun on 2018/11/23.
 */
@Data
@ApiModel(description = "查询过滤条件")
public class SearchFilter {

    @ApiModelProperty(value = "字段名称;", required = true, position = 1)
    @NotBlank(message = "字段名称不能为空")
    private String field;

    @ApiModelProperty(value = "字段值;如果多个值用 [\"value1\",\"value2\",..]", required = true, position = 2)
    @NotNull(message = "字段值不能为空")
    private Object value;

    @ApiModelProperty(value = "操作符", required = true, position = 3, example = "EQ")
    @NotNull(message = "操作符不能为空")
    private Operator operator = Operator.EQ;
}
