package com.yyj.framework.es.search.tcp.model;

import com.yyj.framework.common.util.request.PageQo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Created by yangyijun on 2018/11/23.
 */
@Data
public class SearchQo extends PageQo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "索引名称", required = true, position = 1, example = "graph_cmb_dev")
    @NotEmpty(message = "索引名称不能为空")
    private Set<String> indexs = new HashSet<>();

    @ApiModelProperty(value = "表名列表", position = 2)
    private Set<String> types = new HashSet<>();

    @ApiModelProperty(value = "返回结果字段过滤", position = 3)
    private Set<String> hitFields = new HashSet<>();

    @ApiModelProperty(value = "返回结果高亮字段过滤", position = 4)
    private Set<String> highlightFields = new HashSet<>();

    @ApiModelProperty(value = "聚合参数", position = 5)
    private List<AggSearchQo> aggSearchQos = new LinkedList<>();
}
