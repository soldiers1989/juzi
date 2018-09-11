package com.jzfq.retail.core.api.service;

import com.jzfq.retail.bean.domain.OrdersBase;
import com.jzfq.retail.bean.domain.OrdersProduct;
import com.jzfq.retail.bean.vo.res.TakeCodeOrderRes;

import java.util.List;

/**
 * @Author caishijian@juzifenqi.com
 * @Date 2018年08月03日 10:23
 * @Description: 商品库存service接口
 */
public interface GoodsStockService {

    /**
     * 根据提货码查询订单信息
     * @param tokeCode 提货码
     * @return
     */
    TakeCodeOrderRes getOrderInfo(String tokeCode,Integer sellerId);

    void pickUpGoods(String seqNum,String orderSn);

    void forzenStock(OrdersBase ordersBase);

    /**
     * 扣减库存
     * @param productGoodsId
     */
    void deductAndUpate(Integer productGoodsId);

    void preservePhotograph(String orderSn, List<byte[]> byteArrayList, List<String> suffixs);

    void unFrozenStock(OrdersBase ordersBase, OrdersProduct ordersProduct);
}
