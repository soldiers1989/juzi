package com.jzfq.retail.bean.vo.res;

import com.jzfq.retail.bean.domain.SysPermissionDTO;
import com.jzfq.retail.bean.domain.SysRole;
import com.jzfq.retail.bean.domain.SysUser;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class LoginInfo implements Serializable {
    private SysUser userInfo;
    private List<SysRole> roleList;
    private SysPermissionDTO permissionTree;
    private String ticket;
}
