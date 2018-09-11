package com.jzfq.retail.core.config.filter;

import com.jzfq.retail.bean.vo.res.TouchResponseModel;
import com.jzfq.retail.common.util.JsonMapper;
import com.jzfq.retail.common.util.JwtHelper;
import com.jzfq.retail.bean.vo.res.ResponseModel;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by liuxueliang on 2017/3/13.
 * swagger controller filter
 */
public class AdminLoginFilter implements Filter {

    @Value("${jwt.admin.sec}")
    private String JWT_SEC;
    @Value("${jwt.admin.tokenName}")
    private String JWT_ADMIN_TOKEN_NAME;
    @Value("${login.useFilter}")
    private boolean LOGIN_USE_FILTER;

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
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT");
        response.addHeader("Access-Control-Allow-Headers", "Content-Type, accessToken");
        response.setHeader("Content-type", "text/html;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        String method = request.getMethod();
        if(!"OPTIONS".equals(method)){
            //登录过滤器，使用
            if(LOGIN_USE_FILTER){
                boolean isLogin = false;
                String token = request.getHeader(JWT_ADMIN_TOKEN_NAME);
                if(StringUtils.isNotBlank(token)){
                    Claims claims = JwtHelper.parseJWT(token,JWT_SEC);
                    // 没有过期
                    if(claims != null){
                        isLogin = true;
                    }
                }
                if(isLogin){ //已登录
                    filterChain.doFilter(servletRequest,servletResponse);
                }else{
                    // 如果要绕过登录，那么把下面的代码都注释了，就留这句filterChain.doFilter,
                    // 不然这里用了response.getWriter().write,接口里在返回就会
                    // 报错："getWriter() has already been called for this response"
//                    filterChain.doFilter(servletRequest,servletResponse);
                    TouchResponseModel responseModel = new TouchResponseModel();
                    responseModel.setErrorCode("401");
                    responseModel.setMsg("登录失败");
                    responseModel.setData(null);
                    responseModel.setResult("0");
                    response.getWriter().write(JsonMapper.nonDefaultMapper().toJson(responseModel));
                }
                // 登录过滤器，不使用
            }else{
                filterChain.doFilter(servletRequest,servletResponse);
            }
        }

    }

    @Override
    public void destroy() {

    }
}
