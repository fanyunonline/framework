package com.yyj.framework.common.util.response;

import com.yyj.framework.common.util.exception.BaseException;
import com.yyj.framework.common.util.status.Status;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.text.MessageFormat;

/**
 * Created by yangyijun on 2018/5/2.
 */
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(description = "不分页响应内容")
public class Response<T> {

    @ApiModelProperty(value = "请求是否成功：true（成功）；false（失败）", example = "true")
    protected boolean success = true;

    @ApiModelProperty(value = "请求错误信息")
    protected Message message;

    @ApiModelProperty(value = "请求响应数据")
    protected Payload<T> payload;

    public Response() {
    }

    public Response(T data) {
        if (null == this.getPayload()) {
            this.payload = new Payload<>();
        }
        this.payload.setData(data);
    }

    public static Response success() {
        return new Response();
    }

    public static <T> Response success(T data) {
        return new Response(data);
    }

    public static Response error(Message message) {
        Response response = new Response();
        response.setSuccess(false);
        response.setMessage(message);
        return response;
    }

    public static Response error(BaseException ex) {
        Response response = new Response();
        response.setSuccess(false);
        response.setMessage(buildMessage(ex));
        return response;
    }

    public static Response error(Status status) {
        Response response = new Response();
        response.setSuccess(false);
        response.setMessage(buildMessage(status));
        return response;
    }

    public static Response error(Status status, Object... args) {
        Response response = new Response();
        response.setSuccess(false);
        response.setMessage(buildMessage(status, args));
        return response;
    }

    protected static Message buildMessage(BaseException ex) {
        Message message = new Message();
        message.setCode(ex.getCode());
        message.setDesc(ex.getDesc());
        return message;
    }

    protected static Message buildMessage(Status status) {
        Message message = new Message();
        message.setCode(status.getCode());
        message.setDesc(status.getDesc());
        return message;
    }

    protected static Message buildMessage(Status status, Object... args) {
        Message message = new Message();
        message.setCode(status.getCode());
        message.setDesc(format(status.getDesc(), args));
        return message;
    }

    private static String format(String pattern, Object... arguments) {
        return MessageFormat.format(pattern, arguments);
    }
}
