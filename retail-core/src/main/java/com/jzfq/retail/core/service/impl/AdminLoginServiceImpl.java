package com.jzfq.retail.core.service.impl;

import com.jzfq.retail.bean.domain.SysPermissionDTO;
import com.jzfq.retail.bean.domain.SysRole;
import com.jzfq.retail.bean.domain.SysUser;
import com.jzfq.retail.bean.vo.res.LoginInfo;
import com.jzfq.retail.common.enmu.TouchApiCode;
import com.jzfq.retail.common.util.JsonMapper;
import com.jzfq.retail.common.util.JwtHelper;
import com.jzfq.retail.common.util.MD5Util;
import com.jzfq.retail.core.api.exception.TouchCodeException;
import com.jzfq.retail.core.api.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Slf4j
@Service
public class AdminLoginServiceImpl implements AdminLoginService {
    @Value("${jwt.admin.ttlMillis}")
    private int JWT_TTLMILLIS;
    @Value("${jwt.admin.sec}")
    private String JWT_SEC;
    @Value("${spring.redis.timeout}")
    private int REDIS_TIMEOUT;
    @Autowired
    SysUserService sysUserService;
    @Autowired
    SysRoleService sysRoleService;
    @Autowired
    SysPermissionService sysPermissionService;
    @Autowired
    RedisService redisService;

    @Override
    public LoginInfo login(String loginName, String password) {
        // 非空验证
        // 非空验证：登录名
        if(StringUtils.isBlank(loginName)){
            throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_1000);
        }
        log.debug("loginName:{} 验证登录名非空完成", loginName);
        // 非空验证：密码
        if(StringUtils.isBlank(password)){
            throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_1001);
        }
        log.debug("password:{} 验证登录密码非空完成", password);

        // 正确性验证
        // 数据库查询
        SysUser byNickName = sysUserService.getByNickName(loginName);
        if(byNickName == null){
            throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_1002);
        }
        log.debug("byNickName:{}", new JsonMapper().toJson(byNickName));
        if(!byNickName.getPassword().equals(MD5Util.getMD5String(password))){
            throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_1003);
        }
        // 登录成功，返回登录信息对象
        return getLoginInfo(loginName, byNickName);
    }

    @Override
    public boolean logout(String ticket) {
        return false;
    }

    @Override
    public LoginInfo getLoginInfo(String loginName, SysUser byNickName) {
        log.debug("进入方法：{}","getLoginInfo");
        if(StringUtils.isBlank(loginName)){
            return null;
        }
        logout(loginName);
//        // 从redis中获取loginInfo
//        String loginInfoJson = redisService.getH(loginName, "loginInfo");
//        if(StringUtils.isNotBlank(loginInfoJson)){
//            return new JsonMapper().fromJson(loginInfoJson, LoginInfo.class);
//        }
        // 构建loginInfo
        LoginInfo loginInfo = new LoginInfo();
        log.debug("构建loginInfo");
        // 用户信息
        if(byNickName == null){
            byNickName = sysUserService.getByNickName(loginName);
            if(byNickName == null){
                return null;
            }
        }
        loginInfo.setUserInfo(byNickName);
        log.debug("用户信息查询完成");
        // 角色信息
        List<SysRole> roleList = getRoleList(byNickName.getRoles());
        loginInfo.setRoleList(roleList);
        log.debug("角色信息查询完成");
        // 权限树
        List<Integer> permissionIds = getPermissionIds(roleList);
        log.debug("permissionIds:{}",permissionIds);
        SysPermissionDTO permissionTree = sysPermissionService.getTree(permissionIds);
        loginInfo.setPermissionTree(permissionTree);
        log.debug("权限信息查询完成");
        // ticket
        String token = JwtHelper.createJWT(loginName, JWT_TTLMILLIS, JWT_SEC);
        loginInfo.setTicket(token);
        log.debug("token:{}", token);
        // 保存到redis中
        redisService.setTimesData(loginName, new JsonMapper().toJson(loginInfo), REDIS_TIMEOUT);
        log.debug("保存rides完成");
        return loginInfo;
    }

    /**
     * 通过角色编码拼串获取角色集合
     * @param roles
     * @return
     */
    private List<SysRole> getRoleList(String roles){
        log.debug("角色：{}", roles);
        if(StringUtils.isBlank(roles)){
            return null;
        }
        String[] split = roles.split(",");
        List<SysRole> roleList = new ArrayList<>();
        for(String code : split){
            SysRole byCode = sysRoleService.getByCode(code);
            if(byCode != null){
                roleList.add(byCode);
            }
        }
        log.debug("roleList size:{}", roleList.size());
        return roleList;
    }

    /**
     * 通过用户角色拼串获取去重复的权限ID集合
     * @param roleList
     * @return
     */
    private List<Integer> getPermissionIds(List<SysRole> roleList){
        if(CollectionUtils.isEmpty(roleList)){
            return null;
        }
        List<Integer> permissionIds = new ArrayList<>();
        for(SysRole sysRole : roleList){
            String resourceIds = sysRole.getResourceIds();
            String[] split = resourceIds.split(",");
            for(String str : split){
                Integer id = Integer.parseInt(str);
                if(!permissionIds.contains(id)){
                    permissionIds.add(id);
                }
            }
        }
        return permissionIds;
    }
}
