package com.jzfq.retail.core.config.filter;

import com.jzfq.retail.common.exception.BadRequestException;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.servlet.*;
import java.io.IOException;

/**
 * Created by liuxueliang on 2017/3/13.
 * swagger controller filter
 */
public class CloseApiFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this,
                filterConfig.getServletContext());
    }

    /**
     * 从request中获取token，验证是否已登录
     *      已登录条件：  token不为空，token没有过期
     *      未登录：    返回401
     * @param servletRequest
     * @param servletResponse
     * @param filterChain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
       throw new BadRequestException("接口已关闭");
    }

    @Override
    public void destroy() {

    }
}
