package com.yyj.framework.hbase.search.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by yangyijun on 2018/11/10.
 */
@Data
public class HBaseSearchQo {

    @ApiModelProperty(value = "hbase表命名空间")
    @NotBlank(message = "hbase表命名空间不能为空")
    private String database;

    @ApiModelProperty(value = "hbase表名")
    @NotBlank(message = "hbase表名不能为空")
    private String table;

    @ApiModelProperty(value = "zookeeper主机及端口；格式：hbase://192.168.1.16,192.168.1.17,192.168.1.18:2181")
    private String url;

    @ApiModelProperty(value = "记录rowkeys")
    @NotEmpty(message = "记录rowkeys不能为空")
    private Set<String> rowKeys = new HashSet<>();

    @Min(value = 1)
    @ApiModelProperty(value = "当前页", example = "1")
    private int pageNo = 1;

    @Min(value = 1)
    @ApiModelProperty(value = "每页显示大小", example = "10")
    private int pageSize = 10;
}
