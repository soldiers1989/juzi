package com.jzfq.retail.core.api.service;


import com.jzfq.retail.bean.domain.ProductCateBrandAreas;
import com.jzfq.retail.bean.vo.req.ProductCateBrandAreasReq;
import com.jzfq.retail.bean.vo.req.ProductCateBrandAreasSearchReq;
import com.jzfq.retail.bean.vo.res.ListResultRes;

import java.util.List;
import java.util.Map;

/**
 * @Author lizhe lizhe@juzifenqi.com
 * @Date 2018年06月28日 15:03
 * @Description: 分类-品牌-城市-区间价：操作接口
 */
public interface ProductCateBrandAreasService {

    /**
     * 筛选获取列表
     * 支持分页
     *
     * @param page
     * @param pageSize
     * @param search
     * @return
     */

    ListResultRes<Map<String, Object>> getList(Integer page, Integer pageSize, ProductCateBrandAreasSearchReq search);

    /**
     * 添加
     *
     * @param entity
     */
    void create(ProductCateBrandAreasReq entity, String username);

    /**
     * 修改
     * 该修改操作必须是 0：创建 的时候才可以修改
     *
     * @param entity
     */
    void update(ProductCateBrandAreasReq entity);

    /**
     * 删除--根据ID删除
     *
     * @param id
     */
    void delete(Integer id);

    /**
     * 通过id获取对象
     *
     * @param id
     * @return
     */
    ProductCateBrandAreas getEntityById(Integer id);

    /**
     * 审核操作
     *
     * @param id       编号
     * @param opt      操作 success:通过 fail:失败
     * @param userName 操作人
     */
    void operationAuditing(Integer id, String opt, String userName);
}
