package com.yyj.framework.common.util.request;

import com.yyj.framework.common.util.sort.Direction;
import com.yyj.framework.common.util.sort.Sort;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangyijun on 2018/5/3.
 */
@Data
@ApiModel(description = "分页请求信息")
public class PageQo implements Serializable {

    @Min(value = 1)
    @ApiModelProperty(value = "当前页", example = "1", position = 30)
    protected int pageNo = 1;

    @Min(value = 0)
    @ApiModelProperty(value = "每页显示大小", example = "10", position = 31)
    protected int pageSize = 10;

    @ApiModelProperty(value = "排序字段列表", position = 32)
    protected List<Sort> sorts = new ArrayList<>();

    public void addSort(String property, Direction direction) {
        this.sorts.add(new Sort(property, direction));
    }
}
