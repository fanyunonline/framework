package com.yyj.framework.es.search.tcp.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by yangyijun on 2018/11/23.
 */
@Data
@ApiModel(value = "FilterSearchQo")
public class FilterSearchQo extends SearchQo implements Serializable {

    private static final long serialVersinUID = 1L;

    @ApiModelProperty(value = "逻辑与；所有条件都要满足才返回", position = 10)
    private List<EsSearchFilter> must = new LinkedList<>();

    @ApiModelProperty(value = "逻辑或；至少满足一个条件就返回", position = 11)
    private List<EsSearchFilter> should = new LinkedList<>();

    @ApiModelProperty(value = "逻辑非；所有条件都不满足才返回", position = 12)
    private List<EsSearchFilter> mustNot = new LinkedList<>();
}
