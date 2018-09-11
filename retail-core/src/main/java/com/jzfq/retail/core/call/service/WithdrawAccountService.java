package com.jzfq.retail.core.call.service;

import com.jzfq.retail.core.call.domain.WithdrawCash;

import java.util.Map;

/**
 * @Author liuxueliang@juzifenqi.com
 * @Date 2018年08月31日 18:00
 * @Description: 结算接口
 */
public interface WithdrawAccountService {

    /**
     * 商户放款流水核账
     * @param orderSn   订单号
     */
    void checkConfirm(String orderSn);

    /**
     * 商户账户提现
     * @param withdrawCash
     */
    void withdrawCash(WithdrawCash withdrawCash);

    /**
     * 商户资金账户查询
     * @param businessIds   商户编号，用逗号分隔
     */
    Map<String, Object> getMerchantAccount(String businessIds);
}
