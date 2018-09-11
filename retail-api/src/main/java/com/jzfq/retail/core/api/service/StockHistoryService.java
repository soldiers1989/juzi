package com.jzfq.retail.core.api.service;

import com.jzfq.retail.bean.domain.StockHistory;
import com.jzfq.retail.bean.vo.res.ListResultRes;

import java.util.List;

/**
 * @Author zhangjianwei@juzifenqi.com
 * @Description: 库位接口
 */
public interface StockHistoryService {

    /**
     * 返回所有列表
     * @param search 查询条件
     * @return
     */
    List<StockHistory> getAllList(StockHistory search);

    /**
     * 分页条件查询
     * @param page 当前页码
     * @param pageSize 每页多少
     * @param search 筛选条件
     * @return
     */
    ListResultRes<StockHistory> getList(Integer page, Integer pageSize, StockHistory search);

    /**
     * 添加
     * @param entity
     */
    void create(StockHistory entity);

    /**
     * 修改
     * @param entity
     */
    void update(StockHistory entity);

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
    StockHistory getEntityById(Integer id);
}
