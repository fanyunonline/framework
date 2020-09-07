package com.yyj.framework.es.admin.rest.mapping;

import com.yyj.framework.es.admin.rest.model.IndexAdminQo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by yangyijun on 2017/12/01.
 */
@Data
public class IndexMappingQo extends IndexAdminQo implements Serializable {

    @ApiModelProperty(value = "索引分片数", example = "5")
    private Integer numberOfShards;

    @ApiModelProperty(value = "索引副本数", example = "1")
    private Integer numberOfReplicas;

}
