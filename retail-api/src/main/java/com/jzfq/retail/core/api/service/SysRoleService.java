package com.jzfq.retail.core.api.service;


import com.jzfq.retail.bean.domain.SysRole;
import com.jzfq.retail.bean.domain.SysRoleDTO;
import com.jzfq.retail.bean.domain.SysRoleQuery;
import com.jzfq.retail.bean.vo.req.SysRoleSearch;
import com.jzfq.retail.bean.vo.req.SysRoleVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;


public interface SysRoleService {
    boolean save(SysRoleVo sysRoleVo);
    boolean update(SysRoleVo sysRoleVo);
    String batchRemove(String ids);
    List<SysRole> getByProject(String project);
    List<SysRole> getByName(String name);
    SysRole getByNameEqual(String name);
    SysRole getByCode(String code);
    List<SysRoleDTO> getByPage(SysRoleSearch sysRoleSearch);
    String getResourceNames(String resourceIds);
    int getPageCount(SysRoleSearch sysRoleSearch);
    List<Map<String, Object>> getOptionsAll();
    void removeById(Integer id);
}
