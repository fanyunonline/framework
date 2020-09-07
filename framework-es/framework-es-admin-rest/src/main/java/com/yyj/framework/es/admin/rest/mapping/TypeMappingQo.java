package com.yyj.framework.es.admin.rest.mapping;

import com.yyj.framework.es.admin.rest.model.IndexAdminQo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by yangyijun on 2017/11/30.
 */
@Data
public class TypeMappingQo extends IndexAdminQo implements Serializable {

    @ApiModelProperty(value = "索引type", example = "tv_user")
    @NotBlank(message = "索引type不能为空")
    private String type;

    @ApiModelProperty(value = "字段类型映射", example = "graph_cmb_dev")
//    @NotEmpty(message = "字段类型映射不能为空")
    private Map<String, Field> properties = new LinkedHashMap<>();
}
