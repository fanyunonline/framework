package com.yyj.framework.common.util.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.yyj.framework.common.util.exception.BaseException;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import static com.yyj.framework.common.util.response.Response.buildMessage;


/**
 * Created by yangyijun on 2018/5/2.
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(description = "分页查询响应的内容")
public class PageResponse<T> {

    @ApiModelProperty(value = "请求是否成功：true（成功）；false（失败）", example = "true")
    protected boolean success = true;

    @ApiModelProperty(value = "请求错误信息")
    protected Message message;

    @ApiModelProperty(value = "请求响应数据")
    protected PagePayload<T> payload;

    public PageResponse() {
    }

    public PageResponse(long total, int pageNo, int pageSize) {
        this.payload = new PagePayload(total, pageNo, pageSize);
    }

    public PageResponse(T data, long total, int pageNo, int pageSize) {
        this(total, pageNo, pageSize);
        payload.setData(data);
    }

    public static <T> PageResponse success(PagePayload pagePayload) {
        PageResponse pageResponse = new PageResponse();
        pageResponse.setPayload(pagePayload);
        return pageResponse;
    }

    public static PageResponse success() {
        return new PageResponse();
    }

    public static PageResponse error() {
        PageResponse response = new PageResponse();
        response.setSuccess(false);
        return response;
    }

    public static PageResponse error(BaseException ex) {
        PageResponse response = new PageResponse();
        response.setSuccess(false);
        response.setMessage(buildMessage(ex));
        return response;
    }

    public static PageResponse error(Message message) {
        PageResponse response = new PageResponse();
        response.setSuccess(false);
        response.setMessage(message);
        return response;
    }
}
