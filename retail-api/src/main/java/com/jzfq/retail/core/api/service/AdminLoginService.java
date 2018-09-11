package com.jzfq.retail.core.api.service;

import com.jzfq.retail.bean.domain.SysUser;
import com.jzfq.retail.bean.vo.res.LoginInfo;

public interface AdminLoginService {

    LoginInfo login(String loginName, String password);

    boolean logout(String ticket);

    LoginInfo getLoginInfo(String loginName, SysUser byNickName);
}
