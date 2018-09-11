package com.jzfq.retail.core.api.service;


import com.jzfq.retail.bean.domain.SysPermission;
import com.jzfq.retail.bean.domain.SysPermissionDTO;

import java.util.List;

public interface SysPermissionService {
    SysPermission findById(Integer id);
    boolean save(SysPermission sysPermission);
    int updateMulti(List<SysPermission> sysPermissions);
    int deleteMulti(String ids);
    SysPermission getByParentIdAndLabel(Integer parentId, String label);
    SysPermissionDTO getTree2();
    SysPermissionDTO getTree(List<Integer> idList);
    List<SysPermissionDTO> getListAll();
    List<SysPermissionDTO> getByParentId(List<SysPermissionDTO> all, Integer parentId);
    SysPermissionDTO getById(List<SysPermissionDTO> all, Integer id);
    void setChildren(List<SysPermissionDTO> all, List<SysPermissionDTO> children);
    void setChildren2(List<SysPermissionDTO> children, List<Integer> idList);
}
