package com.jzfq.retail.core.api.service;

import com.jzfq.retail.bean.vo.req.OrdersSettleSearch;
import com.jzfq.retail.bean.vo.req.WithdrawCashCallbackReq;
import com.jzfq.retail.bean.vo.req.WithdrawCashReq;
import com.jzfq.retail.bean.vo.res.ListResultRes;
import com.jzfq.retail.bean.vo.res.OrdersSettleRes;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Title: OrdersSettleService
 * @Company: 北京桔子分期电子商务有限公司
 * @Author Li Zhe lizhe@juzifenqi.com
 * @Date 2018年08月16日 15:44
 * @Description: 商户结算列表service操作
 */
public interface OrdersSettleService {

    /**
     * 获取商户列表
     *
     * @param page
     * @param pageSize
     * @param search
     * @return
     */
    ListResultRes<OrdersSettleRes> getList(Integer page, Integer pageSize, OrdersSettleSearch search);

    List<OrdersSettleRes> getListAll(OrdersSettleSearch search);

    /**
     * 新增
     *
     * @param orderId     订单id
     * @param orderSn     订单编号
     * @param productCost 成本价 sum(orders_product.商家成本价)
     * @param settleMoney 平台结算价=sum(orders_product.settle_money)
     */
    void create(Integer orderId, String orderSn, BigDecimal productCost, BigDecimal settleMoney);

    /**
     * 修改状态
     * @param orderSn
     * @param state
     */
    void modifyState(String orderSn, Integer state);

    /**
     *  确认对账
     * @param orderSn
     */
    void balanceAccount(String orderSn);

    /**
     * 确认放款
     * @param orderSn
     */
    void pendingMoney(String orderSn);

    /**
     * 账户列表
     * @param page
     * @param pageSize
     * @param sellerName
     * @return
     */
    ListResultRes<OrdersSettleRes> getAccountList(Integer page, Integer pageSize, String sellerName);

    /**
     * 提现
     * @param req
     */
    void withdrawCash(WithdrawCashReq req, String username);

    /**
     * 提现回调
     * @param req
     */
    void withdrawCaseCallback(WithdrawCashCallbackReq req);
}
