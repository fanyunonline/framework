package com.yyj.framework.es.search.tcp.query;

import com.yyj.framework.es.search.tcp.model.EsSearchFilter;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by yangyijun on 2018/11/23.
 */
@Data
public class FilterQuery extends Query {

    @ApiModelProperty(value = "逻辑与；所有条件都要满足才返回")
    private List<EsSearchFilter> must = new LinkedList<>();

    @ApiModelProperty(value = "逻辑或；至少满足一个条件就返回")
    private List<EsSearchFilter> should = new LinkedList<>();

    @ApiModelProperty(value = "逻辑非；所有条件都不满足才返回")
    private List<EsSearchFilter> mustNot = new LinkedList<>();
}
