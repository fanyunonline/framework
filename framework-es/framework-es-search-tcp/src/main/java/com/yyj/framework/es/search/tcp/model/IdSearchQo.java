package com.yyj.framework.es.search.tcp.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by yangyijun on 2018/11/23.
 */
@Data
@ApiModel(value = "IdSearchQo")
public class IdSearchQo extends SearchQo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键列表", required = true, position = 10)
    @NotEmpty(message = "主键列表不能为空")
    private Set<String> ids = new HashSet<>();
}
