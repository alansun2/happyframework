package com.alan344happyframework.config.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author AlanSun
 * @date 2020/7/30 17:58
 * <p>
 * https://blog.csdn.net/asd1098626303/article/details/82149129
 **/
public class UndefinedServletFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) {
        // nothing to do
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        chain.doFilter(new UndefinedRequestWrapper((HttpServletRequest) req), res);
    }
}
