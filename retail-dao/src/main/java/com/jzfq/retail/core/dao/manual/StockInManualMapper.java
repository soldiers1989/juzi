package com.jzfq.retail.core.dao.manual;


import com.github.pagehelper.Page;
import com.jzfq.retail.bean.domain.PurchaseCollectGoods;
import com.jzfq.retail.bean.vo.req.PurchaseCollectGoodsReq;

/**
 * @Author zhangjianwei@juzifenqi.com
 * @Description:
 */
public interface StockInManualMapper {
    /**
     * 分页查询 pagehelper 使用
     *
     * @param record
     * @return
     */
    Page<PurchaseCollectGoods> findList(PurchaseCollectGoodsReq record);
}