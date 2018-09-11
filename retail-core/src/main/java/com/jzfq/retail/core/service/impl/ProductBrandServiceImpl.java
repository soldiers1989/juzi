package com.jzfq.retail.core.service.impl;

import com.jzfq.retail.bean.domain.ProductBrandQuery;
import com.jzfq.retail.bean.domain.SysUser;
import com.jzfq.retail.common.enmu.TouchApiCode;
import com.jzfq.retail.common.util.TransferUtil;
import com.jzfq.retail.core.api.exception.TouchCodeException;
import com.jzfq.retail.core.api.service.SysUserService;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.stereotype.Service;
import com.github.pagehelper.PageHelper;
import com.jzfq.retail.bean.domain.ProductBrand;
import com.jzfq.retail.bean.vo.req.ProductBrandReq;
import com.jzfq.retail.bean.vo.req.ProductBrandSearchReq;
import com.jzfq.retail.bean.vo.res.ListResultRes;
import com.jzfq.retail.common.enmu.ProductBrandStatus;
import com.jzfq.retail.core.api.service.ProductBrandService;
import com.jzfq.retail.core.dao.ProductBrandMapper;
import com.jzfq.retail.core.dao.manual.ProductBrandManualMapper;
import org.springframework.beans.factory.annotation.Autowired;
import com.github.pagehelper.Page;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author lizhe lizhe@juzifenqi.com
 * @Date 2018年06月26日 13:40
 * @Description: 商品品牌分类service接口实现
 */
@Service
public class ProductBrandServiceImpl implements ProductBrandService {

    @Autowired
    private ProductBrandMapper productBrandMapper;

    @Autowired
    private ProductBrandManualMapper productBrandManualMapper;

    @Autowired
    private SysUserService sysUserService;

    @Override
    public List<ProductBrand> getAllList(ProductBrandSearchReq search) {
        List<ProductBrand> result = productBrandManualMapper.findList(search).getResult();
        return result;
    }

    @Override
    public ListResultRes<ProductBrand> getList(Integer page, Integer pageSize, ProductBrandSearchReq search) {
        PageHelper.startPage(page, pageSize);
        Page<ProductBrand> listPage = productBrandManualMapper.findList(search);
        return ListResultRes.newListResult(listPage.getResult(), listPage.getTotal(), listPage.getPageNum(), listPage.getPageSize());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(ProductBrandReq src, String username) {
        if (src == null) {
            throw new RuntimeException("添加对象不可以为空");
        }
        ProductBrand target = new ProductBrand();
        TransferUtil.transferIgnoreNull(src, target);
        //获取登录用户
        SysUser byNickName = sysUserService.getByNickName(username);
        target.setCreateId(byNickName.getId());
        target.setCreateTime(new Date());
        target.setState(ProductBrandStatus.CREATE.getCode());
        productBrandMapper.insert(target);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ProductBrandReq src, String username) {
        ProductBrand target = getEntityById(src.getId());
        if (target == null) {
            throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_0049);
        }
        //获取登录用户
        SysUser byNickName = sysUserService.getByNickName(username);
        TransferUtil.transferIgnoreNull(src, target);
        target.setUpdateTime(new Date());
        target.setUpdateId(byNickName.getId());
        productBrandMapper.updateByPrimaryKey(target);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id, String username) {
        ProductBrand updateEntity = getEntityById(id);
        if (updateEntity != null) {
            //获取登录用户
            SysUser byNickName = sysUserService.getByNickName(username);
            updateEntity.setUpdateTime(new Date());
            updateEntity.setUpdateId(byNickName.getId());
            updateEntity.setState(ProductBrandStatus.DELETE.getCode());
            productBrandMapper.updateByPrimaryKey(updateEntity);
        }
    }

    @Override
    public ProductBrand getEntityById(Integer id) {
        if (id == null) {
            throw new RuntimeException("缺少参数");
        }
        return productBrandMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<ProductBrand> getProductBrandByProductCateId(Integer productCateId) {
        ProductBrandQuery query = new ProductBrandQuery();
        query.createCriteria().andProductCateIdEqualTo(productCateId);
        return productBrandMapper.selectByExample(query);
    }

    @Override
    public List<Map<String, Object>> getOptions(Integer cateId) {
        ProductBrandQuery query = new ProductBrandQuery();
        query.createCriteria().andProductCateIdEqualTo(cateId);
        List<ProductBrand> productBrandList = productBrandMapper.selectByExample(query);
        return productBrandList.stream().map(productBrand -> {
            Map<String, Object> map = new HashedMap();
            map.put("value", productBrand.getId());
            map.put("label", productBrand.getName());
            return map;
        }).collect(Collectors.toList());
    }
}
