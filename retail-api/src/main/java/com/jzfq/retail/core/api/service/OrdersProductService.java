package com.jzfq.retail.core.api.service;

import com.jzfq.retail.bean.domain.OrdersProduct;

/**
 * @Title: OrdersProductService
 * @Company: 北京桔子分期电子商务有限公司
 * @Author Li Zhe lizhe@juzifenqi.com
 * @Date 2018年08月03日 14:51
 * @Description: 订单产品信息service操作接口
 */
public interface OrdersProductService {

    /**
     * 通过订单编号orderSn获取订单产品信息
     *
     * @param orderSn
     * @return
     */
    OrdersProduct getByOrderSn(String orderSn);
}
