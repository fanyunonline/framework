package com.yyj.framework.es.admin.tcp.model;

import com.yyj.framework.common.util.constant.CrudType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangyijun on 2017/12/29.
 */
@Data
public class DocQo extends IndexAdminQo implements Serializable {

    @ApiModelProperty(value = "es type名称")
    private String type;

    @ApiModelProperty(value = "es 操作类型")
    @NotNull(message = "操作类型不能为空")
    private CrudType crudType = CrudType.D;

    @ApiModelProperty(value = "操作数据列表")
    private List<Source> sources = new ArrayList<>();

}
