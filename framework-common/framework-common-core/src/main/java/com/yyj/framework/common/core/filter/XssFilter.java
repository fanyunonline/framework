package com.yyj.framework.common.core.filter;

import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@WebFilter(urlPatterns = "/*", filterName = "xssFilter", dispatcherTypes = DispatcherType.REQUEST)
public class XssFilter implements Filter {
    private static final String DEFAULT_ENCODING = "UTF-8";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        new XssDefenseRequestWrapper(request);
        filterChain.doFilter(request, servletResponse);
    }

    @Override
    public void destroy() {
    }

    private static final class XssDefenseRequestWrapper extends HttpServletRequestWrapper {
        public XssDefenseRequestWrapper(HttpServletRequest request) throws IOException {
            super(request);
        }

        @Override
        public String getHeader(String name) {
            return HtmlUtils.htmlEscape(super.getHeader(name), DEFAULT_ENCODING);
        }

        @Override
        public String getParameter(String name) {
            return HtmlUtils.htmlEscape(super.getParameter(name), DEFAULT_ENCODING);
        }

        @Override
        public Map<String, String[]> getParameterMap() {
            Map<String, String[]> parameterMap = super.getParameterMap();
            if (parameterMap != null) {
                return parameterMap.entrySet().stream().map(entry -> {
                    String key = entry.getKey();
                    String[] array = entry.getValue();
                    if (array != null && array.length > 0)
                        array = Arrays.stream(array).map(str -> HtmlUtils.htmlEscape(str, DEFAULT_ENCODING)).collect(Collectors.toList()).toArray(new String[array.length]);
                    return Pair.of(key, array != null ? array : new String[0]);
                }).collect(Collectors.toMap(Pair::getFirst, Pair::getSecond));
            }
            return null;
        }

        @Override
        public String[] getParameterValues(String name) {
            String[] values = super.getParameterValues(name);
            if (values != null && values.length > 0)
                return Arrays.stream(values).map(str -> HtmlUtils.htmlEscape(str, DEFAULT_ENCODING)).collect(Collectors.toList()).toArray(new String[values.length]);
            return values;
        }
    }
}
