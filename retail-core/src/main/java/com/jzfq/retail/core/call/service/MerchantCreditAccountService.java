package com.jzfq.retail.core.call.service;

import com.jzfq.retail.core.call.domain.MerchantAccountInfoResult;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Title: MerchantCreditAccountService
 * @Company: 北京桔子分期电子商务有限公司
 * @Author Li Zhe lizhe@juzifenqi.com
 * @Date 2018年07月03日 15:03
 * @Description: 商户信用账户操作接口
 */
public interface MerchantCreditAccountService {

    /**
     * 商户信用账户开户
     *
     * @param approvedDate    风控审批通过时间
     * @param businessId      商户id
     * @param firstMonthQuota 首月授信额度
     * @param monthQuota      每月授信额度
     * @param signDateStart   签约开始日期
     * @param totalQuota      授信总额度
     */
    void initMerchantAccount(Date approvedDate, String businessId, BigDecimal firstMonthQuota, BigDecimal monthQuota, Date signDateStart, BigDecimal totalQuota);

    /**
     * 获取商户信用账户信息
     *
     * @param businessId 商户id
     * @return
     */
    MerchantAccountInfoResult getMerchantAccountInfo(String businessId);

}
