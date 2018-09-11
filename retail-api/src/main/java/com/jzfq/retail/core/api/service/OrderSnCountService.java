package com.jzfq.retail.core.api.service;

/**
 * @Author Liuwei@juzifenqi.com
 * @Date 2018年07月6日 17:43
 * @Description: 订单号计数数据接口
 */
public interface OrderSnCountService {


    /**
     * 获取订单编号orderSn
     *
     * @param sellerId 商户id
     * @return 生成的订单号
     */
    String getOrderSn(Integer sellerId);
}
