package com.jzfq.retail.core.api.service;


import com.jzfq.retail.bean.domain.OrdersBase;
import com.jzfq.retail.bean.domain.OrdersUser;
import com.jzfq.retail.bean.vo.req.CapitalBackReq;
import com.jzfq.retail.bean.vo.res.ListResultRes;

import java.util.List;
import java.util.Map;

/**
 * @Author zhangjianwei@juzifenqi.com
 * @Date 2018年07月02日 14:43
 * @Description: 订单接口
 */
public interface OrdersUserService {

    /**
     * 插入订单用户
     *
     * @param ordersUser
     */
    void saveOrderUser(OrdersUser ordersUser);

    /**
     * 通过订单编号获取订单用户
     *
     * @param orderSn
     * @return
     */
    OrdersUser getByOrderSn(String orderSn);


    OrdersUser getByOrderId(Integer orderId);
}
