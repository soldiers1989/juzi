package com.jzfq.retail.core.service.impl;

import com.jzfq.retail.bean.domain.SysPermission;
import com.jzfq.retail.bean.domain.SysPermissionDTO;
import com.jzfq.retail.bean.domain.SysPermissionQuery;
import com.jzfq.retail.common.enmu.TouchApiCode;
import com.jzfq.retail.common.util.JsonMapper;
import com.jzfq.retail.common.util.TransferUtil;
import com.jzfq.retail.core.api.exception.TouchCodeException;
import com.jzfq.retail.core.api.service.RedisService;
import com.jzfq.retail.core.api.service.SysPermissionService;
import com.jzfq.retail.core.dao.SysPermissionMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class SysPermissionServiceImpl implements SysPermissionService {

    @Autowired
    RedisService redisService;

    @Autowired
    SysPermissionMapper sysPermissionMapper;

    @Override
    public SysPermission findById(Integer id) {
        return sysPermissionMapper.selectByPrimaryKey(id);
    }

    @Override
    public boolean save(SysPermission sysPermission){
        saveValid(sysPermission);
        int i = sysPermissionMapper.insert(sysPermission);
        if(i == 1){
            refreshTreeRedis();
            return true;
        }
        return false;
    }

    @Override
    public int updateMulti(List<SysPermission> sysPermissions){
        int count = 0;
        for(SysPermission sysPermission : sysPermissions){
            updateValid(sysPermission);
            count = count + sysPermissionMapper.updateByPrimaryKey(sysPermission);
        }
        // 更新缓存
        refreshTreeRedis();
        return count;
    }

    @Override
    public int deleteMulti(String ids){
        List<SysPermissionDTO> listAll = getListAll();
        int count = 0;
        for(String idStr : ids.split(",")){
            count = count + delete(listAll, Integer.parseInt(idStr));
        }
        // 更新缓存
        refreshTreeRedis();
        return count;
    }

    @Override
    public SysPermission getByParentIdAndLabel(Integer parentId, String label){
        SysPermissionQuery query = new SysPermissionQuery();
        SysPermissionQuery.Criteria criteria = query.createCriteria();
        criteria.andParentIdEqualTo(parentId);
        criteria.andLabelEqualTo(label);
        List<SysPermission> sysPermissionList = sysPermissionMapper.selectByExample(query);
        if(sysPermissionList == null || sysPermissionList.size() == 0){
            return null;
        }
        return sysPermissionList.get(0);
    }

    @Override
    public SysPermissionDTO getTree2(){
        log.debug("进入getTree2方法");
        Object redisObj = redisService.queryObjectByKey("sysPermissionDTOJson");
        log.debug("redisObj:{}", new JsonMapper().toJson(redisObj));
        if(redisObj != null){
            log.debug("从redis中取值");
            String json = (String) redisObj;
            SysPermissionDTO sysPermissionDTO = new JsonMapper().fromJson(json, SysPermissionDTO.class);
            return sysPermissionDTO;
        }
        log.debug("从数据库中取值");
        return refreshTreeRedis();
    }

    @Override
    public SysPermissionDTO getTree(List<Integer> idList){
        if(CollectionUtils.isEmpty(idList)){
            return null;
        }
        SysPermissionDTO tree = getTree2();
        List<SysPermissionDTO> children = tree.getChildren();
        setChildren2(children, idList);
        return tree;
    }

    @Override
    public List<SysPermissionDTO> getListAll(){
        SysPermissionQuery query = new SysPermissionQuery();
        List<SysPermission> sysPermissionList = sysPermissionMapper.selectByExample(query);
        log.debug("sysPermissionList:{}" + new JsonMapper().toJson(sysPermissionList));
        List<SysPermissionDTO> sysPermissionDTOList = new ArrayList<>();
        for(SysPermission sysPermission : sysPermissionList){
            SysPermissionDTO sysPermissionDTO = new SysPermissionDTO();
            TransferUtil.transfer(sysPermissionDTO, sysPermission);
            sysPermissionDTOList.add(sysPermissionDTO);
        }
        log.debug("sysPermissionDTOList:{}" + new JsonMapper().toJson(sysPermissionDTOList));
        return sysPermissionDTOList;
    }

    @Override
    public List<SysPermissionDTO> getByParentId(List<SysPermissionDTO> all, Integer parentId){
        List<SysPermissionDTO> children = new ArrayList<>();
        for(SysPermissionDTO sysPermissionDTO : all){
            if(parentId.toString().equals(sysPermissionDTO.getParentId()+"")){
                children.add(sysPermissionDTO);
            }
        }
        return children;
    }

    @Override
    public SysPermissionDTO getById(List<SysPermissionDTO> all, Integer id){
        for(SysPermissionDTO sysPermissionDTO : all){
            if(Integer.parseInt(sysPermissionDTO.getId().toString()) == id){
                return sysPermissionDTO;
            }
        }
        return null;
    }

    @Override
    public void setChildren(List<SysPermissionDTO> all, List<SysPermissionDTO> children){
        if(!CollectionUtils.isEmpty(children)){
            for(SysPermissionDTO sysPermissionDTO : children){
                Integer parentId = sysPermissionDTO.getId();
                List<SysPermissionDTO> children2 = getByParentId(all, parentId);
                sysPermissionDTO.setChildren(children2);
                setChildren(all, children2);
            }
        }
    }

    @Override
    public void setChildren2(List<SysPermissionDTO> children, List<Integer> idList){
        if(!CollectionUtils.isEmpty(children)){
            List<SysPermissionDTO> tmpList = new ArrayList<>();
            for(int i=0;i<children.size();i++){
                tmpList.add(children.get(i));
            }
            for(int i=0;i<tmpList.size();i++){
                SysPermissionDTO sysPermissionDTO = tmpList.get(i);
                if(!idList.contains(sysPermissionDTO.getId()) || !sysPermissionDTO.getAvailable()){
                    children.remove(sysPermissionDTO);
                }else{
                    setChildren2(sysPermissionDTO.getChildren(), idList);
                }
            }
        }
    }

    private int delete(List<SysPermissionDTO> all, Integer id){
        SysPermissionDTO sysPermissionDTO = getById(all, id);
        List<Integer> allChildrenIds = getAllChildrenIds(all, sysPermissionDTO.getChildren());
        allChildrenIds.add(sysPermissionDTO.getId());
        return deleteByIds(allChildrenIds);
    }

    private List<Integer> getAllChildrenIds(List<SysPermissionDTO> all, List<SysPermissionDTO> children){
        List<Integer> ids = new ArrayList<>();
        if(!CollectionUtils.isEmpty(children)){
            for(SysPermissionDTO sysPermissionDTO : children){
                ids.add(sysPermissionDTO.getId());
                Integer parentId = sysPermissionDTO.getId();
                List<SysPermissionDTO> children2 = getByParentId(all, parentId);
                getAllChildrenIds(all, children);
            }
        }
        return ids;
    }

    private int deleteByIds(List<Integer> ids){
        SysPermissionQuery query = new SysPermissionQuery();
        SysPermissionQuery.Criteria criteria = query.createCriteria();
        criteria.andIdIn(ids);
        return sysPermissionMapper.deleteByExample(query);
    }


    private SysPermissionDTO refreshTreeRedis(){
        log.debug("进入refreshTreeRedis方法");
        List<SysPermissionDTO> all = getListAll();
        SysPermissionDTO sysPermissionDTO = getById(all, 0);
        log.debug("sysPermissionDTO:" + new JsonMapper().toJson(sysPermissionDTO));
        List<SysPermissionDTO> children = getByParentId(all, 0);
        sysPermissionDTO.setChildren(children);
        setChildren(all, children);
        String sysPermissionDTOJson = new JsonMapper().toJson(sysPermissionDTO);
        redisService.setForeverData("sysPermissionDTOJson", sysPermissionDTOJson);
        log.debug("refreshTreeRedis保存redis完成");
        return sysPermissionDTO;
    }

    /**
     * 验证：新增接口
     * @param sysPermission
     */
    private void saveValid(SysPermission sysPermission){
        // 非空验证：参数对象
        if(sysPermission == null){
            throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_1300);
        }
        // 非空验证：父节点
        if(sysPermission.getParentId() == null){
            throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_1301);
        }
        // 正确性校验: 父节点必需存在
        SysPermission byParentId = findById(sysPermission.getParentId());
        if(byParentId == null){
            throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_1302);
        }
        // 非空验证：节点名称
        if(StringUtils.isBlank(sysPermission.getLabel())){
            throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_1303);
        }
        // 唯一性校验： 父节点下不能有同名子节点
        SysPermission byParentIdAndLabel = getByParentIdAndLabel(sysPermission.getParentId(), sysPermission.getLabel());
        if(byParentIdAndLabel != null){
            throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_1304);
        }
    }

    /**
     * 验证：编辑接口
     * @param sysPermission
     */
    private void updateValid(SysPermission sysPermission){
        // 参数对象
        if(sysPermission == null){
            throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_1350);
        }
        // 非空验证：ID不能为空
        if(sysPermission.getId() == null){
            throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_1351);
        }
        // 非空验证：ID必需存在
        SysPermission byId = findById(sysPermission.getId());
        if(byId == null){
            throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_1352);
        }
        // 唯一性校验: 父节点必需存在
        if(sysPermission.getParentId() != -1){
            SysPermission byParentId = findById(sysPermission.getParentId());
            if(byParentId == null){
                throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_1353);
            }
        }

        // 唯一性校验： 父节点下不能有同名子节点
        if(StringUtils.isNotBlank(sysPermission.getLabel()) && !sysPermission.getLabel().equals(byId.getLabel())){
            Integer parentId = null;
            if(sysPermission.getParentId() != null){
                parentId = sysPermission.getParentId();
            }else{
                parentId = byId.getParentId();
            }
            SysPermission byParentIdAndLabel = getByParentIdAndLabel(parentId, sysPermission.getLabel());
            if(byParentIdAndLabel != null){
                throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_1354);
            }
        }
    }

}
