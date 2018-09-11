package com.jzfq.retail.core.service.impl;

import com.jzfq.retail.bean.domain.ProductCateQuery;
import com.jzfq.retail.bean.domain.SysUser;
import com.jzfq.retail.common.enmu.TouchApiCode;
import com.jzfq.retail.core.api.exception.TouchCodeException;
import com.jzfq.retail.core.api.service.SysUserService;
import com.jzfq.retail.core.dao.manual.ProductBrandManualMapper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import com.github.pagehelper.PageHelper;
import com.jzfq.retail.bean.domain.ProductCate;
import com.jzfq.retail.bean.vo.req.ProductCateReq;
import com.jzfq.retail.bean.vo.req.ProductCateSearchReq;
import com.jzfq.retail.bean.vo.res.ListResultRes;
import com.jzfq.retail.core.api.service.ProductCateService;
import com.jzfq.retail.core.dao.ProductCateMapper;
import com.jzfq.retail.core.dao.manual.ProductCateManualMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import com.github.pagehelper.Page;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author lizhe lizhe@juzifenqi.com
 * @Date 2018年06月26日 9:56
 * @Description: 商品分类接口实现
 */
@Service
public class ProductCateServiceImpl implements ProductCateService {

    @Autowired
    private ProductCateMapper productCateMapper;

    @Autowired
    private ProductCateManualMapper productCateManualMapper;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private ProductBrandManualMapper productBrandManualMapper;

    @Override
    public ListResultRes<Map<String, Object>> getList(Integer page, Integer pageSize, ProductCateSearchReq search) {
        PageHelper.startPage(page, pageSize);
        Page<Map<String, Object>> listPage = productCateManualMapper.findList(search);
        return ListResultRes.newListResult(listPage.getResult(), listPage.getTotal(), listPage.getPageNum(), listPage.getPageSize());
    }

    private void validNameUniqueUpdate(String productCateName, String oldProductCateName){
        if(!oldProductCateName.equals(productCateName)){ // 修改了名称才验证
            ProductCate endityByName = getEndityByName(productCateName);
            if(endityByName != null){
                throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_1004);
            }
        }
    }

    private void validNameUniqueSave(String productCateName){
        ProductCate endityByName = getEndityByName(productCateName);
        if(endityByName != null){
            throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_1004);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(ProductCateReq entity, String userName) {
        if (entity == null) {
            throw new RuntimeException("添加对象不可以为空");
        }
        validNameUniqueSave(entity.getName());
        SysUser byNickName = sysUserService.getByNickName(userName);
        ProductCate saveEntity = new ProductCate();
        BeanUtils.copyProperties(entity, saveEntity);
        saveEntity.setCreateId(byNickName.getId());
        saveEntity.setUpdateId(byNickName.getId());
        saveEntity.setCreateTime(new Date());
        saveEntity.setUpdateTime(new Date());
        saveEntity.setVisible(1);
        productCateMapper.insert(saveEntity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ProductCateReq entity, String userName) {
        if (entity == null || entity.getId() == null) {
            throw new RuntimeException("缺少参数");
        }
        ProductCate updateEntity = getEntityById(entity.getId());
        if (updateEntity == null) {
            throw new RuntimeException("修改实例不存在");
        }
        if(StringUtils.isNotBlank(entity.getName())){
            validNameUniqueUpdate(entity.getName(), updateEntity.getName());
        }
        BeanUtils.copyProperties(entity, updateEntity);
        SysUser byNickName = sysUserService.getByNickName(userName);
        updateEntity.setUpdateId(byNickName.getId());
        updateEntity.setUpdateTime(new Date());
        productCateMapper.updateByPrimaryKey(updateEntity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) {
        ProductCate productCate = getEntityById(id);
        productCate.setVisible(2);
        productCateMapper.updateByPrimaryKey(productCate);
        // 逻辑删除品牌
        productBrandManualMapper.deleteByCateId(id);
    }

    @Override
    public ProductCate getEntityById(Integer id) {
        if (id == null) {
            throw new RuntimeException("缺少参数");
        }
        return productCateMapper.selectByPrimaryKey(id);
    }

    @Override
    public ProductCate getEndityByName(String name) {
        ProductCateQuery query = new ProductCateQuery();
        ProductCateQuery.Criteria criteria = query.createCriteria();
        criteria.andNameEqualTo(name);
        List<ProductCate> productCates = productCateMapper.selectByExample(query);
        if(!CollectionUtils.isEmpty(productCates)){
            return productCates.get(0);
        }
        return null;
    }

    @Override
    public List<Map<String, Object>> getOptions() {
        ProductCateQuery query = new ProductCateQuery();
        List<ProductCate> productCateList = productCateMapper.selectByExample(query);
        return productCateList.stream().map(productCate -> {
            Map<String, Object> map = new HashedMap();
            map.put("value", productCate.getId());
            map.put("label", productCate.getName());
            return map;
        }).collect(Collectors.toList());
    }

    @Override
    public List<Map<String, Object>> getOptionsBySellerId(Integer sellerId) {
        List<Map<String, Object>> options = productCateManualMapper.getOptionsBySellerId(sellerId);
        return options;
    }
}
