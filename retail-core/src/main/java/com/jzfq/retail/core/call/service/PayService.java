package com.jzfq.retail.core.call.service;


import com.jzfq.retail.core.call.domain.BankCardResult;
import com.jzfq.retail.core.call.domain.WXPay;

import java.util.Map;

/**
 * @Title: PersonCreditAccountService
 * @Company: 北京桔子分期电子商务有限公司
 * @Author liuwei@juzifenqi.com
 * @Date 2018年07月09日 15:03
 * @Description: 支付系统操作接口
 */
public interface PayService {

    /**
     * 获取个人绑卡信息 - 请求丁浩接口
     */
    BankCardResult getBankCard(String customerId, String capitalSide);

    /**
     * 微信小程序或公众号支付
     *
     * @param pay
     */
    String wxPaymentConfirm(WXPay pay);

}
