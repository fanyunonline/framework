package com.yyj.framework.es.search.tcp.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by yangyijun on 2018/11/23.
 */
@Data
@ApiModel(value = "FullTextSearchQo")
public class FullTextSearchQo extends SearchQo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "全文检索关键字", required = true, position = 10)
    @NotBlank(message = "全文检索关键字不能为空")
    private String keyword;

    @ApiModelProperty(value = "匹配字段列表", required = true, position = 11)
    @NotEmpty(message = "匹配字段列表不能为空")
    private Set<MatchField> matchFields = new HashSet<>();

    @Data
    public static class MatchField {

        @ApiModelProperty(value = "匹配字段名称", required = true, position = 1)
        @NotBlank(message = "匹配字段名称不能为空")
        private String name;

        @ApiModelProperty(value = "匹配字段权重", example = "1", position = 2)
        private float boost = 1f;

        @ApiModelProperty(value = "匹配关键字可以间隔词条数", example = "1", position = 3)
        private int slop = 1;
    }
}
