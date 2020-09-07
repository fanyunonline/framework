package com.yyj.framework.es.admin.rest.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * Created by yangyijun on 2018/12/13.
 */
@Data
public class IndexAdminQo {

    @ApiModelProperty(value = "索引名称", example = "graph_cmb_dev")
    @NotBlank(message = "索引名称不能为空")
    protected String index;

    @ApiModelProperty(value = "集群主机IP及端口", example = "192.168.1.49,192.168.1.50:9300")
    protected String nodes;

    @ApiModelProperty(value = "集群名称", example = "framework")
    protected String clusterName;

}
