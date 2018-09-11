package com.jzfq.retail.core.config.filter;


import com.jzfq.retail.bean.vo.res.ResponseModel;
import com.jzfq.retail.bean.vo.res.TouchResponseModel;
import com.jzfq.retail.common.enmu.TouchApiCode;
import com.jzfq.retail.common.util.JsonMapper;
import com.jzfq.retail.common.util.JwtHelper;
import com.jzfq.retail.core.api.service.RedisService;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by liuwei on 2018/7/1.
 * swagger controller filter
 */
@Slf4j
public class SellerLoginFilter implements Filter {

    @Value("${jwt.seller.sec}")
    private String JWT_SEC;
    @Value("${jwt.seller.tokenName}")
    private String JWT_TOKEN_NAME;
    @Value("${login.useFilter}")
    private boolean LOGIN_USE_FILTER;

    @Autowired
    private RedisService redisService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this,
                filterConfig.getServletContext());
    }

    /**
     * 从request中获取token，验证是否已登录
     * 已登录条件：  token不为空，token没有过期
     * 未登录：    返回401
     *
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
        response.addHeader("Access-Control-Allow-Headers", "Content-Type");
        response.setHeader("Content-type", "text/html;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        //登录过滤器，使用
        if (LOGIN_USE_FILTER) {
            boolean isLogin = false;
            String token = request.getHeader(JWT_TOKEN_NAME);
            if (StringUtils.isNotBlank(token)) {
                Claims claims = JwtHelper.parseJWT(token, JWT_SEC);
                // 没有过期
                if (claims != null) {
                    //1、解析token，判断openID是否在数据库中存在或是否在redis中存在
                    String tokenStr = (String) claims.get("openID");
                    log.info("seller ------ tokenStr:{}", tokenStr);
                    String[] str = tokenStr.split("@");
                    log.info("str.length:{}", str.length);
                    if (str.length == 3 && StringUtils.isNotBlank(str[2])) {
                        if (redisService.queryObjectByKey(str[2]) != null) {
                            //BD在后台清除商户密码，或变更密码时会删除Redis，如果Redis没有删除，则说明密码没有变更过
                            isLogin = true;
                        }
                    }
                }
            }
            isLogin = true;
            log.info("seller ------ isLogin:{}", isLogin);
            if (isLogin) { //已登录
                filterChain.doFilter(servletRequest, servletResponse);
            } else {
                TouchResponseModel model = new TouchResponseModel();
                model.setResult("0");
                model.setErrorCode(TouchApiCode.TOUCH_API_CODE_9999.getCode());
                model.setMsg(TouchApiCode.TOUCH_API_CODE_9999.getMsg());
                response.getWriter().write(JsonMapper.nonDefaultMapper().toJson(model));
            }
            // 登录过滤器，不使用
        } else {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    @Override
    public void destroy() {

    }
}
