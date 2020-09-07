package com.yyj.framework.es.search.tcp.model;

import com.yyj.framework.es.search.tcp.constant.AggType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by yangyijun on 2018/11/25.
 */
@Data
public class AggSearchQo implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "聚合结果别名", required = true, position = 0)
    @NotBlank(message = "聚合结果别名不能为空")
    private String key;

    @ApiModelProperty(value = "聚合字段", required = true, position = 1)
    @NotBlank(message = "聚合字段不能为空")
    private String field;

    @ApiModelProperty(value = "聚合类型；默认值：MAX", required = true, position = 2)
    private @NotBlank(message = "聚合类型不能为空") AggType aggType = AggType.MAX;

    @ApiModelProperty(value = "聚合配置", position = 3)
    private Map<String, Object> options = new LinkedHashMap<>();

    @ApiModelProperty(value = "子聚合", position = 4)
    private List<AggSearchQo> subs = new LinkedList<>();

}
