package com.jzfq.retail.core.api.service;

import com.jzfq.retail.bean.domain.PurchaseCollectGoods;
import com.jzfq.retail.bean.vo.req.PurchaseCollectGoodsReq;
import com.jzfq.retail.bean.vo.res.ListResultRes;
import com.jzfq.retail.bean.vo.res.StockInItemRes;
import com.jzfq.retail.bean.vo.res.StockInRes;

import java.util.List;

/**
 * @Author zhangjianwei@juzifenqi.com
 * @Description: 入库单查询接口
 */
public interface StockInService {

    /**
     * 返回所有列表
     * @param search 查询条件
     * @return
     */
    List<StockInRes> getAllList(PurchaseCollectGoodsReq search);

    /**
     * 分页条件查询
     * @param page 当前页码
     * @param pageSize 每页多少
     * @param search 筛选条件
     * @return
     */
    ListResultRes<StockInRes> getList(Integer page, Integer pageSize, PurchaseCollectGoodsReq search);

    /**
     * 添加
     * @param entity
     */
    void create(PurchaseCollectGoods entity);

    /**
     * 修改
     * @param entity
     */
    void update(PurchaseCollectGoods entity);

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
    PurchaseCollectGoods getEntityById(Integer id);
}
