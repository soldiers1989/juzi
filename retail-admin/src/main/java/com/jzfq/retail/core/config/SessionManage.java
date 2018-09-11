package com.jzfq.retail.core.config;

import com.jzfq.retail.common.util.JwtHelper;
import com.jzfq.retail.core.api.service.RedisService;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <B>文件名称：</B>SessionManage<BR>`
 * <B>文件描述：</B><BR>
 * <BR>
 * <B>版权声明：</B>(C)2016-2018<BR>
 * <B>公司部门：</B>东方银谷 研发二部 CBG<BR>
 * <B>创建时间：</B>2017/03/14<BR>
 *
 * @author 吕宏业  lvhongye@yingu.com
 * @version 1.0
 **/
@Service
public class SessionManage {

    @Value("${jwt.admin.sec}")
    private String JWT_ADMIN_SEC;// 后台登录JWT
    @Value("${jwt.admin.tokenName}")
    private String JWT_ADMIN_TOKEN_NAME;// 后台登录JWT
    @Value("${login.useFilter}")
    private boolean LOGIN_USE_FILTER;

    @Value("${jwt.seller.sec}")
    private String JWT_SELLER_SEC;// 商户登录JWT
    @Value("${jwt.seller.tokenName}")
    private String JWT_SELLER_TOKEN_NAME;// 商户登录JWT


    @Autowired
    private RedisService  redisService;

    public HttpServletRequest getHttpServletRequest(){
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    public HttpServletResponse getHttpServletResponse(){
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
    }

    /**
     * 获取后台登录用户的token
     * @return
     */
    public String getToken(){
        HttpServletRequest request = getHttpServletRequest();
        return request.getHeader(JWT_ADMIN_TOKEN_NAME);
    }

    /**
     * 获取后台登录用户名
     * @returnop
     * busi
     */
    public String getUserName(){
        String token = getToken();
        if(StringUtils.isNotBlank(token)){
            Claims claims = JwtHelper.parseJWT(token,JWT_ADMIN_SEC);
            if (claims != null) {
                return (String) claims.get("username");
            }
        }
        return "";
    }

    /**
     * 删除后台登录用户Redis
     */
    public void del(){
        String key = getUserName();
        redisService.deleteByKey(key);
    }

    public boolean isLOGIN_USE_FILTER() {
        return LOGIN_USE_FILTER;
    }

    public void setLOGIN_USE_FILTER(boolean LOGIN_USE_FILTER) {
        this.LOGIN_USE_FILTER = LOGIN_USE_FILTER;
    }

    //------------------------------------
    /**
     * 从request中获取商户登录用户的token
     * @return
     */
    public String getSellerToken(){
        HttpServletRequest request = getHttpServletRequest();
        return request.getParameter(JWT_SELLER_TOKEN_NAME);
    }

    /**
     * 获取商户登录用户的tokenStr
     * @return
     */
    public String[] getSellerTokenStr(){
        String token = getSellerToken();
        if (StringUtils.isNotBlank(token)) {
            Claims claims = JwtHelper.parseJWT(token, JWT_SELLER_SEC);
            if (claims != null) {
                String tokenStr = (String) claims.get("openID");
                String[] str = tokenStr.split("-");
                return str;
            }
        }
        return null;
    }

    /**
     * 获取商户登录OPENID
     * 商户token格式：sellerMobile-sellerLogin-openID;
     * @return
     */
    public String getSellerOpenID() {
        String[] str = getSellerTokenStr();
        if (str.length == 3 && StringUtils.isNotBlank(str[2])) {
            return str[2];
        }
        return "";
    }

    /**
     * 获取商户登录sellerLogin 商户登录用户的商户编码
     * 商户token格式：sellerMobile-sellerLogin-openID;
     * @return
     */
    public String getSellerLogin() {
        String[] str = getSellerTokenStr();
        if (str.length == 3 && StringUtils.isNotBlank(str[1])) {
            return str[1];
        }
        return "";
    }

    /**
     * 获取商户登录sellerMobile
     * 商户token格式：sellerMobile-sellerLogin-openID;
     * @return
     */
    public String getSellerMobile() {
        String[] str = getSellerTokenStr();
        if (str.length == 3 && StringUtils.isNotBlank(str[0])) {
            return str[0];
        }
        return "";
    }
    //------------------------------------
}
