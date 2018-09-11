package com.jzfq.retail.core.service;

import com.jzfq.retail.common.util.JwtHelper;
import com.jzfq.retail.core.api.service.RedisService;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * <B>文件名称：</B>SessionManage<BR>
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
    private String JWT_SEC;
    @Value("${jwt.admin.tokenName}")
    private String JWT_TOKEN_NAME;
    @Value("${login.useFilter}")
    private boolean LOGIN_USE_FILTER;


    @Autowired
    private RedisService redisService;
//    @Autowired
//    private RoleManage roleManage;
//    @Autowired
//    private UserManage userManage;

    public HttpServletRequest getHttpServletRequest(){
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    public HttpServletResponse getHttpServletResponse(){
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
    }

    public String getToken(){
        HttpServletRequest request = getHttpServletRequest();
        return request.getParameter(JWT_TOKEN_NAME);
    }

    public String getUserName(){
        String token = getToken();
        if(StringUtils.isNotBlank(token)){
            Claims claims = JwtHelper.parseJWT(token,JWT_SEC);
            if (claims != null) {
                return (String) claims.get("username");
            }
        }
        return "";
    }

//    public Role getRole(){
//        String empNo = getUserName();
//        return roleManage.getRoleByEmpNo(empNo);
//    }

    public String getMemberObj(){
        String key = getUserName();
        if(StringUtils.isNotBlank(key)){
            return redisService.queryStringByKey(key);
        }
        return "";
    }

//    public OrgAndHr getOrgAndHrObj(){
//        //不使用登录过滤器时开启
//        if (!LOGIN_USE_FILTER) {
//            //注释：由于登陆接口未完成，暂时使用下面模拟登陆用户方式调试
//            OrgAndHr orgAndHr = get();
//            return orgAndHr;
//        }
//        String orgAndHrJson = getMemberObj();
//        if(StringUtils.isNotBlank(orgAndHrJson)){
//            List<OrgAndHr> orgAndHrs = new JsonMapper().fromJsonToList(orgAndHrJson,OrgAndHr.class);
//            if (CollectionUtils.isNotEmpty(orgAndHrs)){
//                return orgAndHrs.get(0);
//            }
//        }
//        return null;
//    }

//    public HrData getHrData(){
//        OrgAndHr orgAndHr = getOrgAndHrObj();
//        if(orgAndHr != null){
//            return orgAndHr.getHr();
//        }
//        return null;
//    }

    /**
     * 已登录，request中有token时使用
     * @return
     */
//    public Map<String,Object> getHrOrgTokenMap(){
//        Map<String,Object> map = new HashMap<>();
//        map.put("token", getToken());
//        map.put("orgData", getOrgData());
//        map.put("hrData", getHrData());
//        return map;
//    }

    /**
     * 登录时用，request中还没有token呢
     * @param orgAndHrJson
     * @param token
     * @return
     */
//    public Map<String,Object> getHrOrgTokenMap(String token, String orgAndHrJson){
//        OrgData orgData = null;
//        HrData hrData = null;
//        if(StringUtils.isNotBlank(orgAndHrJson)){
//            List<OrgAndHr> orgAndHrs = new JsonMapper().fromJsonToList(orgAndHrJson,OrgAndHr.class);
//            if (orgAndHrs != null && orgAndHrs.size() > 0){
//                OrgAndHr orgAndHr = orgAndHrs.get(0);
//                orgData = orgAndHr.getOrg();
//                hrData = orgAndHr.getHr();
//            }
//        }
//        Map<String,Object> map = new HashMap<>();
//        map.put("token", token);
//        map.put("orgData", orgData);
//        map.put("hrData", hrData);
//        return map;
//    }

    /**
     * 登录时用，request中还没有token呢
     * @param memberJson
     * @param token
     * @return
     */
    public Map<String,Object> getMemberTokenMap(String token, String memberJson){
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("token", token);
        map.put("memberData", memberJson);
        return map;
    }

//    public OrgData getOrgData(){
//        OrgAndHr orgAndHr = getOrgAndHrObj();
//        if(orgAndHr != null){
//            return orgAndHr.getOrg();
//        }
//        return null;
//    }

    /**
     * 注释：由于登陆接口未完成，暂时使用下面模拟登陆用户方式调试
     * @return
     */
//    private OrgAndHr get() {
//        int n = 1;
//        switch (n) {
//            case 1: return setOrgAndHr("kermit", "吕宏业");//申请人
////            case 1: return setOrgAndHr("YG0005285", "吕宏业");//申请人
//            case 2: return setOrgAndHr("YG0005212", "李云龙");//城市经理
//            case 3: return setOrgAndHr("YG0004573", "夏远峰");//区域经理
//            case 4: return setOrgAndHr("YG0004809", "程江浩");//区域市场专员/主管
//            case 5: return setOrgAndHr("YG0004545", "刘卫国");//营销市场支持专员
//            case 6: return setOrgAndHr("YG0004726", "孙瑞普");//市场支持主管
//            case 7: return setOrgAndHr("YG0004422", "于亮");//营销中心负责人
//            case 8: return setOrgAndHr("YG001", "孙敏");//孙总
//            case 9: return setOrgAndHr("YG0004501", "邢学翠");//财务放款
//            case 10: return setOrgAndHr("YG0005285", "吕宏业");//营业部上报实际花销
//            case 11: return setOrgAndHr("YG0004545", "刘卫国");//营销市场支持专员(上报后)
//            case 12: return setOrgAndHr("YG0004726", "孙瑞普");//市场支持主管(上报后)
//            case 13: return setOrgAndHr("YG0004422", "于亮");//营销中心负责人(上报后)
//            case 14: return setOrgAndHr("YG0004501", "邢学翠");//财务销账
//            default: return null;
//        }
//    }

    /**
     * 注释：由于登陆接口未完成，暂时使用下面模拟登陆用户方式调试
     * @return
     */
//    private OrgAndHr setOrgAndHr(String username, String fullname) {
//        OrgAndHr orgAndHr = new OrgAndHr();
//        HrData hr = new HrData();
//        hr.setUsername(username);
//        hr.setE0127(username);
//        hr.setFullname(fullname);
//        orgAndHr.setHr(hr);
//        return orgAndHr;
//    }


    public boolean isLOGIN_USE_FILTER() {
        return LOGIN_USE_FILTER;
    }

    public void setLOGIN_USE_FILTER(boolean LOGIN_USE_FILTER) {
        this.LOGIN_USE_FILTER = LOGIN_USE_FILTER;
    }

//    public User getLoginUser(){
//        String userName = getUserName();
//        if(org.apache.commons.lang3.StringUtils.isBlank(userName)){
//            throw new BadRequestException("登录异常");
//        }
//        User user = userManage.getUserByEmpNo(userName);
//        if(user == null){
//            throw new BadRequestException("用户不存在");
//        }
//        return user;
//    }
}
