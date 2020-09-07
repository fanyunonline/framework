package com.yyj.framework.common.util.sort;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by yangyijun on 2018/6/4.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "请求排序信息")
public class Sort {

    @ApiModelProperty(value = "排序字段属性名称", position = 33)
    private String property;

    @ApiModelProperty(value = "排序方向：ASC(升序)，DESC(降序)；默认值：ASC(升序)", example = "ASC", position = 34)
    private Direction direction = Direction.ASC;
}
