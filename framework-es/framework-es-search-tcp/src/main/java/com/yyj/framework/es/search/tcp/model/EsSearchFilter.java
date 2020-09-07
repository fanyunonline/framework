package com.yyj.framework.es.search.tcp.model;

import com.yyj.framework.common.core.constant.FieldType;
import com.yyj.framework.common.core.model.SearchFilter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * Created by yangyijun on 2018/11/23.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "ES查询过滤条件")
public class EsSearchFilter extends SearchFilter {

    @ApiModelProperty(value = "字段数据类型", required = true, position = 5, example = "STRING")
    @NotNull(message = "字段数据类型不能为空")
    private FieldType fieldType = FieldType.STRING;

    @ApiModelProperty(value = "权重", position = 6, example = "1.0F")
    private float boost;
}
