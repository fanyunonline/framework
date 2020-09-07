package com.yyj.framework.common.core.exception;

import com.alibaba.fastjson.JSON;
import com.yyj.framework.common.util.exception.UnexpectedStatusException;
import com.yyj.framework.common.util.http.RequestWrapper;
import com.yyj.framework.common.util.log.Log;
import com.yyj.framework.common.util.log.LogFactory;
import com.yyj.framework.common.util.response.Message;
import com.yyj.framework.common.util.response.Response;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;


/**
 * Created by yangyijun on 2018/6/8.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Log logger = LogFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response handleException(HttpServletRequest request, IllegalStateException exception) throws Exception {
        return buildError(exception);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response handleException(HttpServletRequest request, IllegalArgumentException exception) throws Exception {
        return buildError(exception);
    }

    //参数绑定异常处理
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response handleException(HttpServletRequest request, BindException exception) throws Exception {
        return buildError(buildMessage(exception.getFieldErrors()));
    }

    //@Valid异常处理
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response handleException(HttpServletRequest request, MethodArgumentNotValidException exception) throws Exception {
        return buildError(buildMessage(exception.getBindingResult().getFieldErrors()));
    }

    //@RequestParam异常处理
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response handleException(HttpServletRequest request, MissingServletRequestParameterException exception) throws Exception {
        StringBuffer sb = new StringBuffer();
        buildLog(sb, exception.getParameterName(), exception.getMessage());
        return buildError(sb.toString());
    }

    @ExceptionHandler(UnexpectedStatusException.class)
    public Response handleException(HttpServletRequest request, HttpServletResponse response,
                                    UnexpectedStatusException ex) throws Exception {
        recordErrorLog(ex, request, ex);
        return buildError(ex);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response handleException(HttpServletRequest request, Exception exception) throws Exception {
        recordErrorLog(exception, request, exception);
        return buildError(exception);
    }

    ///////////////////////
    // private functions
    ///////////////////////
    private void recordErrorLog(Exception ex, HttpServletRequest request, Throwable throwable) throws IOException {
        HttpRequestInfo info = new HttpRequestInfo();
        ServletRequest requestWrapper = new RequestWrapper(request);
        info.setPath(request.getServletPath());
        info.setParameters(requestWrapper.getParameterMap());
        info.setBody(requestWrapper.getReader().lines().reduce((s1, s2) -> s1 + s2).orElse(""));
        logger.error("ErrorCode: {0}\nRequest>>>{1}", throwable, ex.getMessage(), JSON.toJSONString(info));
    }


    private void buildLog(StringBuffer sb, String field, String msg) {
        sb.append("field:[");
        sb.append(field);
        sb.append("] message:[");
        sb.append(msg);
        sb.append("]");
        logger.error(sb.toString());
    }

    private String buildMessage(List<FieldError> fieldErrors) throws IOException {
        StringBuffer sb = new StringBuffer();
        for (FieldError fieldError : fieldErrors) {
            buildLog(sb, fieldError.getField(), fieldError.getDefaultMessage());
        }
        return sb.toString();
    }

    private Response buildError(Exception exception) {
        return buildError(exception.getMessage());
    }

    private Response buildError(Object message) {
        Message msg = new Message();
        msg.setCode(HttpStatus.BAD_REQUEST.value());
        msg.setDesc(message.toString());
        return Response.error(msg);
    }

    @Data
    public static class HttpRequestInfo {
        private String path;
        private Map<String, String[]> parameters;
        private String body;
    }
}
