package com.jzfq.retail.core.api.service;


import com.jzfq.retail.bean.domain.ProductBrand;
import com.jzfq.retail.bean.vo.req.ProductBrandReq;
import com.jzfq.retail.bean.vo.req.ProductBrandSearchReq;
import com.jzfq.retail.bean.vo.res.ListResultRes;

import java.util.List;
import java.util.Map;

/**
 * @Author lizhe lizhe@juzifenqi.com
 * @Date 2018年06月25日 20:33
 * @Description: 商品品牌service操作
 */
public interface ProductBrandService {

    /**
     * 获取列表--无分页
     *
     * @param search
     * @return
     */
    List<ProductBrand> getAllList(ProductBrandSearchReq search);

    /**
     * 获取列表--分页
     *
     * @param page
     * @param pageSize
     * @param search
     * @return
     */
    ListResultRes<ProductBrand> getList(Integer page, Integer pageSize, ProductBrandSearchReq search);

    /**
     * 添加
     *
     * @param entity
     * @param username 添加人
     */
    void create(ProductBrandReq entity, String username);

    /**
     * 修改
     *
     * @param entity
     * @param username 修改人
     */
    void update(ProductBrandReq entity, String username);

    /**
     * 删除--根据ID删除
     *
     * @param id
     */
    void delete(Integer id, String username);

    /**
     * 通过ID获取品牌
     * @param id
     * @return
     */
    ProductBrand getEntityById(Integer id);

    /**
     * 通过品牌的父类ID获取对应列表
     * @param productCateId
     * @return
     */
    List<ProductBrand> getProductBrandByProductCateId(Integer productCateId);

    /**
     * id,name映射
     * @return
     */
    List<Map<String, Object>> getOptions(Integer cateId);
}
