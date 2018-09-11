package com.jzfq.retail.core.service.impl;

import com.jzfq.retail.bean.domain.*;
import com.jzfq.retail.bean.vo.req.SysRoleSearch;
import com.jzfq.retail.bean.vo.req.SysRoleVo;
import com.jzfq.retail.common.enmu.TouchApiCode;
import com.jzfq.retail.common.util.EsapiUtil;
import com.jzfq.retail.common.util.TransferUtil;
import com.jzfq.retail.core.api.exception.TouchCodeException;
import com.jzfq.retail.core.api.service.RedisService;
import com.jzfq.retail.core.api.service.SysPermissionService;
import com.jzfq.retail.core.api.service.SysRoleService;
import com.jzfq.retail.core.dao.SysPermissionMapper;
import com.jzfq.retail.core.dao.SysRoleMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SysRoleServiceImpl extends CommonMethods implements SysRoleService {

    @Autowired
    RedisService redisService;

    @Autowired
    SysRoleMapper sysRoleMapper;

    @Autowired
    SysPermissionMapper sysPermissionMapper;

    @Autowired
    SysPermissionService sysPermissionService;

    /**
     * 新增
     * @param sysRoleVo
     * @return
     */
    public boolean save(SysRoleVo sysRoleVo){
        saveValid(sysRoleVo);
        // 对象转化
        SysRole sysRole = new SysRole();
        TransferUtil.transfer(sysRole, sysRoleVo);
        // 持久化
        int i = sysRoleMapper.insert(sysRole);
        // 返回结果
        if(i == 1){
            return true;
        }
        return false;
    }

    /**
     * 修改
     * @param sysRoleVo
     * @return
     */
    public boolean update(SysRoleVo sysRoleVo){
        updateValid(sysRoleVo);
        // 对象转化
        SysRole sysRole = new SysRole();
        TransferUtil.transfer(sysRole, sysRoleVo);
        // 持久化
        int i = sysRoleMapper.updateByPrimaryKey(sysRole);
        // 返回结果
        if(i == 1){
            return true;
        }
        return false;
    }


    /**
     * 批量删除
     * @param ids
     * @return
     */
    public String batchRemove(String ids){
        int count = 0;
        List<String> failList = new ArrayList<>();
        for(String id : ids.split(",")){
            int i = sysRoleMapper.deleteByPrimaryKey(Integer.parseInt(id));
            if(i == 1){
                count ++;
            }else{
                SysRole byID = sysRoleMapper.selectByPrimaryKey(Integer.parseInt(id));
                failList.add(byID.getName());
            }
        }
        return failMsg(count, ids, failList);
    }
    

    public List<SysRole> getByProject(String project) {
        return getListByParams(null, null, project);
    }

    public List<SysRole> getByName(String name){
        return getListByParams(null, name, null);
    }

    public SysRole getByNameEqual(String name){
        SysRoleQuery query = new SysRoleQuery();
        SysRoleQuery.Criteria criteria = query.createCriteria();
        criteria.andNameEqualTo(name);
        List<SysRole> by = sysRoleMapper.selectByExample(query);
        if(!CollectionUtils.isEmpty(by)){
            return by.get(0);
        }
        return null;
    }

    /**
     * 通过编码查询
     * @param code
     * @return
     */
    public SysRole getByCode(String code){
        List<SysRole> listByParams = getListByParams(code, null, null);
        if(!CollectionUtils.isEmpty(listByParams)){
            return listByParams.get(0);
        }
        return null;
    }

    /**
     * 通过属性查询多个用户，组合查询方式“与”
     * @param code
     * @param name
     * @param project
     * @return
     */
    private List<SysRole> getListByParams(String code, String name, String project){
        SysRoleQuery query = new SysRoleQuery();
        SysRoleQuery.Criteria criteria = query.createCriteria();
        if(StringUtils.isNotBlank(code)){
            criteria.andCodeEqualTo(code);
        }
        if(StringUtils.isNotBlank(name)){
            criteria.andNameLike("%"+ EsapiUtil.sql(name)+"%");
        }
        if(StringUtils.isNotBlank(project)){
            criteria.andProjectEqualTo(project);
        }
        return sysRoleMapper.selectByExample(query);
    }

    /**
     * 分页构建query
     * @param sysRoleSearch
     * @return
     */
    private SysRoleQuery buildQuery(SysRoleSearch sysRoleSearch){
        SysRoleQuery query = new SysRoleQuery();
        SysRoleQuery.Criteria criteria = query.createCriteria();
        if(sysRoleSearch != null){
            if(sysRoleSearch.getStart() == null){
                sysRoleSearch.setStart(0);
            }
            if(sysRoleSearch.getLength() == null){
                sysRoleSearch.setLength(5);
            }
            if(StringUtils.isNotBlank(sysRoleSearch.getCode())){
                criteria.andCodeEqualTo(sysRoleSearch.getCode().trim());
            }
            if(StringUtils.isNotBlank(sysRoleSearch.getName())){
                criteria.andNameLike("%"+sysRoleSearch.getName().trim()+"%");
            }
            if(StringUtils.isNotBlank(sysRoleSearch.getProject())){
                criteria.andProjectEqualTo(sysRoleSearch.getProject().trim());
            }
        }
        return query;
    }

    /**
     * 分页列表
     * @param sysRoleSearch
     * @return
     */
    public List<SysRoleDTO> getByPage(SysRoleSearch sysRoleSearch){
        SysRoleQuery query = buildQuery(sysRoleSearch);
        query.setOrderByClause("id desc");
        List<SysRole> sysRoles = sysRoleMapper.selectByExample(query);
        List<SysRoleDTO> sysRoleDTOList = new ArrayList<>();
        for(SysRole sysRole : sysRoles){
            SysRoleDTO sysRoleDTO = new SysRoleDTO();
            TransferUtil.transfer(sysRoleDTO, sysRole);
            String resourceIds = sysRole.getResourceIds();
            if(StringUtils.isNotBlank(resourceIds)){
                String resourceNames = getResourceNames(resourceIds);
                sysRoleDTO.setResourceNames(resourceNames);
            }
            sysRoleDTOList.add(sysRoleDTO);
        }
        return sysRoleDTOList;
    }

    public String getResourceNames(String resourceIds){
        String[] split = resourceIds.split(",");
        List<Integer> list = new ArrayList<>();
        for(String str : split){
            list.add(Integer.parseInt(str));
        }
        SysPermissionQuery sysPermissionQuery = new SysPermissionQuery();
        SysPermissionQuery.Criteria criteria = sysPermissionQuery.createCriteria();
        criteria.andIdIn(list);
        List<SysPermission> sysPermissionList = sysPermissionMapper.selectByExample(sysPermissionQuery);
        String resourceNames = "";
        if(!CollectionUtils.isEmpty(sysPermissionList)){
            for(int i=0;i<sysPermissionList.size();i++){
                resourceNames += sysPermissionList.get(i).getLabel();
                if(i<sysPermissionList.size()-1){
                    resourceNames += ",";
                }
            }
        }
        return resourceNames;
    }

    /**
     * 分页总数量
     * @param sysRoleSearch
     * @return
     */
    public int getPageCount(SysRoleSearch sysRoleSearch) {
        return sysRoleMapper.countByExample(buildQuery(sysRoleSearch));
    }

    public List<Map<String, Object>> getOptionsAll(){
        SysRoleQuery query = new SysRoleQuery();
        List<SysRole> sysRoleList = sysRoleMapper.selectByExample(query);
        return sysRoleList.stream().map(sysRole -> {
            Map<String, Object> map = new HashedMap();
            map.put("value", sysRole.getCode());
            map.put("label", sysRole.getName());
            return map;
        }).collect(Collectors.toList());
    }

    @Override
    public void removeById(Integer id) {
        sysRoleMapper.deleteByPrimaryKey(id);
    }


    /**
     * 验证：新增接口
     * @param sysRoleVo
     */
    private void saveValid(SysRoleVo sysRoleVo){
        // 参数对象
        if(sysRoleVo == null){
            throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_1200);
        }
        // 非空验证：编码
        if(StringUtils.isBlank(sysRoleVo.getCode())){
            throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_1201);
        }
        // 非空验证：名称
        if(StringUtils.isBlank(sysRoleVo.getName())){
            throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_1202);
        }
        // 非空验证：项目名称
        if(StringUtils.isBlank(sysRoleVo.getProject())){
            throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_1203);
        }
        // 唯一性验证：编码
        if(StringUtils.isNotBlank(sysRoleVo.getCode())){
            SysRole byCode = getByCode(sysRoleVo.getCode());
            if(byCode != null){
                throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_1204);
            }
        }
        // 唯一性校验：名称
        if(StringUtils.isNotBlank(sysRoleVo.getName())){
            SysRole byNameEqual = getByNameEqual(sysRoleVo.getName());
            if(byNameEqual != null){
                throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_1205);
            }
        }
    }

    /**
     * 验证：编辑接口
     * @param sysRoleVo
     */
    private void updateValid(SysRoleVo sysRoleVo){
        // 非空验证：参数对象
        if(sysRoleVo == null){
            throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_1250);
        }
        // 非空验证：ID
        if(sysRoleVo.getId() == null){
            throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_1251);
        }
        SysRole byID = sysRoleMapper.selectByPrimaryKey(sysRoleVo.getId());
        // 唯一性验证：编码
        if(StringUtils.isNotBlank(sysRoleVo.getCode()) && !sysRoleVo.getCode().trim().equals(byID.getCode())){
            SysRole byCode = getByCode(sysRoleVo.getCode());
            if(byCode != null){
                throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_1252);

            }
        }
        // 唯一性验证：名称
        if(StringUtils.isNotBlank(sysRoleVo.getName()) && !sysRoleVo.getName().trim().equals(byID.getName())){
            SysRole byNameEqual = getByNameEqual(sysRoleVo.getName());
            if(byNameEqual != null){
                throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_1253);
            }
        }
    }
}
