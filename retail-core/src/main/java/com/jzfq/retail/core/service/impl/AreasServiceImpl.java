package com.jzfq.retail.core.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.jzfq.retail.bean.domain.AreasQuery;
import com.jzfq.retail.bean.vo.req.AreasSearchReq;
import com.jzfq.retail.bean.vo.res.ListResultRes;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.stereotype.Service;
import com.jzfq.retail.bean.domain.Areas;
import com.jzfq.retail.core.api.service.AreasService;
import com.jzfq.retail.core.dao.AreasMapper;
import com.jzfq.retail.core.dao.manual.AreasManualMapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author lizhe lizhe@juzifenqi.com
 * @Date 2018年06月27日 19:42
 * @Description: 城市接口操作实现
 */
@Service
public class AreasServiceImpl implements AreasService {

    @Autowired
    private AreasMapper areasMapper;

    @Autowired
    private AreasManualMapper areasManualMapper;


    @Override
    public List<Areas> getAreaByParentId(String parentId) {
        if (StringUtils.isEmpty(parentId)) {
            throw new RuntimeException("缺少参数");
        }
        List<Areas> result = areasManualMapper.findAreaListByParentId(parentId);
        return result;
    }

    @Override
    public List<Areas> getAreaByParentId(Areas areas) {

        AreasQuery areasQuery = new AreasQuery();
        areasQuery.createCriteria().andAreaIdEqualTo(areas.getAreaId())
                .andNameEqualTo(areas.getName())
                .andZipCodeEqualTo(areas.getZipCode())
                .andCityCodeEqualTo(areas.getCityCode());
        return areasMapper.selectByExample(areasQuery);
    }

    @Override
    public Areas getAreaByAreaId(String areaId) {
        if(StringUtils.isEmpty(areaId)){
            throw new RuntimeException("缺少参数");
        }
        Areas areas = areasManualMapper.findAreaByAreaId(areaId);
        return areas;
    }

    @Override
    public String getMergerNameByAreaId(String areaId) {
        Areas areas = getAreaByAreaId(areaId);
        if(areas!=null){
            return areas.getMergerName().replace(",","");
        }
        return null;
    }

    @Override
    public String getMergerShortNameByAreaId(String areaId) {
        Areas areas = getAreaByAreaId(areaId);
        if(areas!=null){
            return areas.getMergerShortName().replace(","," ");
        }
        return null;
    }

    @Override
    public List<Map<String, Object>> getOptions(String parentId) {
        AreasQuery query = new AreasQuery();
        query.createCriteria().andParentIdEqualTo(parentId);
        List<Areas> areasList = areasMapper.selectByExample(query);
        return areasList.stream().map(area -> {
            Map<String, Object> map = new HashedMap();
            map.put("value", area.getAreaId());
            map.put("label", area.getName());
            return map;
        }).collect(Collectors.toList());
    }

    @Override
    public ListResultRes<Map<String, Object>> getList(Integer page, Integer pageSize, AreasSearchReq search) {
        PageHelper.startPage(page, pageSize);
        Page<Map<String, Object>> listPage = areasManualMapper.findList(search);
        return ListResultRes.newListResult(listPage.getResult(), listPage.getTotal(), listPage.getPageNum(), listPage.getPageSize());
    }
}
