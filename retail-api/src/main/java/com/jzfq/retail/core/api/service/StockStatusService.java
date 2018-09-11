package com.jzfq.retail.core.api.service;

import com.jzfq.retail.bean.domain.StockStatus;
import com.jzfq.retail.bean.vo.res.ListResultRes;

import java.util.List;

/**
 * @Author zhangjianwei@juzifenqi.com
 * @Description: 库位接口
 */
public interface StockStatusService {

    /**
     * 返回所有列表
     * @param search 查询条件
     * @return
     */
    List<StockStatus> getAllList(StockStatus search);

    /**
     * 分页条件查询
     * @param page 当前页码
     * @param pageSize 每页多少
     * @param search 筛选条件
     * @return
     */
    ListResultRes<StockStatus> getList(Integer page, Integer pageSize, StockStatus search);

    /**
     * 添加
     * @param entity
     */
    void create(StockStatus entity);

    /**
     * 修改
     * @param entity
     */
    void update(StockStatus entity);

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
    StockStatus getEntityById(Integer id);
}
