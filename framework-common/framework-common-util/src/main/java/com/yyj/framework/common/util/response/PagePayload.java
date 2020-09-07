package com.yyj.framework.common.util.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yangyijun
 * @description Response分页响应数据
 * @date 2018/10/16
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "分页响应数据")
public class PagePayload<T> extends Payload<T> {

    @ApiModelProperty(value = "总记录数", example = "100")
    private long total;

    @ApiModelProperty(value = "当前页", example = "1")
    private int pageNo = 1;

    @ApiModelProperty(value = "每页显示记录数", example = "10")
    private int pageSize = 10;

    @ApiModelProperty(value = "总页数", example = "0")
    public long getTotalPages() {
        if (pageSize == 0) {
            return 0;
        } else {
            return (total + pageSize - 1) / pageSize;
        }
    }
}
