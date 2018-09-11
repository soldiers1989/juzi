package com.jzfq.retail.core.api.service;



import com.jzfq.retail.bean.domain.ProductCate;
import com.jzfq.retail.bean.vo.req.ProductCateReq;
import com.jzfq.retail.bean.vo.req.ProductCateSearchReq;
import com.jzfq.retail.bean.vo.res.ListResultRes;

import java.util.List;
import java.util.Map;

/**
 * @Author lizhe lizhe@juzifenqi.com
 * @Date 2018年06月25日 14:06
 * @Description: 商品品类service接口
 */
public interface ProductCateService {

    /**
     * 分页条件查询
     *
     * @param page     当前页码
     * @param pageSize 每页多少
     * @param search   筛选条件
     * @return
     */
    ListResultRes<Map<String, Object>> getList(Integer page, Integer pageSize, ProductCateSearchReq search);

    /**
     * 添加
     *
     * @param entity
     * @param userName 添加人
     */
    void create(ProductCateReq entity, String userName);

    /**
     * 修改
     *
     * @param entity
     * @param userName 修改人
     */
    void update(ProductCateReq entity, String userName);

    /**
     * 根据ID删除
     *
     * @param id
     */
    void delete(Integer id);

    /**
     * 通过ID查询品类
     *
     * @param id
     * @return
     */
    ProductCate getEntityById(Integer id);

    /**
     * 通过名称查询
     * @param name
     * @return
     */
    ProductCate getEndityByName(String name);

    /**
     * id,name映射
     * @return
     */
    List<Map<String, Object>> getOptions();
    List<Map<String, Object>> getOptionsBySellerId(Integer sellerId);
}
