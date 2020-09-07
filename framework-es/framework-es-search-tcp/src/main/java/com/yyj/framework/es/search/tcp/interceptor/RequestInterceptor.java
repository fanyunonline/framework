package com.yyj.framework.es.search.tcp.interceptor;

import com.alibaba.fastjson.JSON;
import com.yyj.framework.common.util.http.RequestWrapper;
import com.yyj.framework.common.util.json.JsonUtils;
import com.yyj.framework.common.util.log.Log;
import com.yyj.framework.common.util.log.LogFactory;
import com.yyj.framework.es.search.tcp.controller.SearchController;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by yangyijun on 2018/7/4.
 */
public class RequestInterceptor implements HandlerInterceptor {

    private static final Log logger = LogFactory.getLogger(SearchController.class);

    @Value("${es.print.input.params.log:false}")
    private boolean printInputParams;

    public RequestInterceptor() {
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        RequestWrapper requestWrapper = new RequestWrapper(request);
        String body = requestWrapper.getBody();
        if (printInputParams && StringUtils.isNotBlank(body)) {
            logger.info("\n" + JSON.toJSONString(JsonUtils.jsonToMap(body), true));
        }
        return true;
    }


    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler, Exception e) throws Exception {

    }

}
