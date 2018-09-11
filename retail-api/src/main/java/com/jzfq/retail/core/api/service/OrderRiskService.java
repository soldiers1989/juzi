package com.jzfq.retail.core.api.service;

import com.juzifenqi.usercenter.entity.member.MemberInfo;
import com.jzfq.retail.bean.domain.OrdersBase;
import com.jzfq.retail.bean.domain.OrdersUser;
import com.jzfq.retail.bean.domain.SellerAddress;
import com.jzfq.retail.bean.vo.req.CommitPayReq;

/**
 * @Title: OrderRiskService
 * @Company: 北京桔子分期电子商务有限公司
 * @Author Li Zhe lizhe@juzifenqi.com
 * @Date 2018年08月08日 11:25
 * @Description: 订单风控操作 -准进 -进件
 */
public interface OrderRiskService {
    /**
     * 订单风控准进调用
     *
     * @param ordersBase
     * @param sellerAddress
     * @param ordersUser
     * @param isSingleCredit
     * @throws Exception
     */
    void riskCheck(OrdersBase ordersBase, SellerAddress sellerAddress, OrdersUser ordersUser, Integer isSingleCredit) throws Exception;

    /**
     * 订单风控进件调用
     *
     * @param ordersBase
     * @param memberInfo
     * @param isSingleCredit
     * @param productCategory
     * @param longitud        经度
     * @param latitude        纬度
     */
    void riskReceive(OrdersBase ordersBase, MemberInfo memberInfo, Integer isSingleCredit, Integer productCategory, Double longitud, Double latitude);

    /**
     * 补单风控进件
     *
     * @param orderSn 订单编号
     */
    void supplementRiskReceive(String orderSn);

    /**
     * 补单风控准进
     *
     * @param orderSn
     * @param isSingleCredit
     */
    void supplementRiskCheck(String orderSn, Integer isSingleCredit) throws Exception;

    //======================
    boolean isCZBT(Integer sellerId);
}
