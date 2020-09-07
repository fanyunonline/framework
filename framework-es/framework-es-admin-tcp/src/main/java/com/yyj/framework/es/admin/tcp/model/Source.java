package com.yyj.framework.es.admin.tcp.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by yangyijun on 2017/12/29.
 */
@Data
public class Source implements Serializable {

    @ApiModelProperty(value = "主键")
    @NotBlank(message = "主键不能为空")
    private String id;

    @ApiModelProperty(value = "字段值")
    private Map<String, Object> source = new LinkedHashMap<>();
}
