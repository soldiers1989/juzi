package com.jzfq.retail.core.api.service;

import com.jzfq.retail.bean.domain.StockAddress;
import com.jzfq.retail.bean.vo.res.ListResultRes;

import java.util.List;

/**
 * @Author zhangjianwei@juzifenqi.com
 * @Description: 库存地址接口
 */
public interface StockAddressService {

    /**
     * 返回所有列表
     * @param search 查询条件
     * @return
     */
    List<StockAddress> getAllList(StockAddress search);

    /**
     * 分页条件查询
     * @param page 当前页码
     * @param pageSize 每页多少
     * @param search 筛选条件
     * @return
     */
    ListResultRes<StockAddress> getList(Integer page, Integer pageSize, StockAddress search);

    /**
     * 添加
     * @param entity
     */
    void create(StockAddress entity);

    /**
     * 修改
     * @param entity
     */
    void update(StockAddress entity);

    /**
     * 根据ID删除
     *
     * @param id
     */
    void delete(Integer id);

    /**
     * 通过ID查询
     * @param id
     * @return
     */
    StockAddress getEntityById(Integer id);
}
