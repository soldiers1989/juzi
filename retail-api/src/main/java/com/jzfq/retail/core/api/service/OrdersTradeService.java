package com.jzfq.retail.core.api.service;

import com.jzfq.retail.bean.domain.OrdersTrade;

/**
 * @Title: OrdersTradeService
 * @Company: 北京桔子分期电子商务有限公司
 * @Author Li Zhe lizhe@juzifenqi.com
 * @Date 2018年08月03日 15:04
 * @Description: XXXX
 */
public interface OrdersTradeService {

    /**
     * 通过订单编号获取该对象
     *
     * @param orderSn
     * @return
     */
    OrdersTrade getByOrderSn(String orderSn);
}
