package com.jzfq.retail.core.call.service;


import com.jzfq.retail.core.call.domain.AssetBankCapital;
import com.jzfq.retail.core.call.domain.AssetBankCapitalResult;
import com.jzfq.retail.core.call.domain.AssetEntryCapital;

/**
 * @Title: AssetPlatformService
 * @Company: 北京桔子分期电子商务有限公司
 * @Author Li Zhe lizhe@juzifenqi.com
 * @Date 2018年07月03日 19:16
 * @Description: 资产平台系统调用操作接口
 */
public interface AssetPlatformService {


    /**
     * 第一次调用资金路由-绑卡
     *
     * @param bankCapital
     */
    AssetBankCapitalResult bankCapital(AssetBankCapital bankCapital);

    /**
     * 第二次调用资金路由-进件
     *
     * @param entryCapital
     */
    void entryCapital(AssetEntryCapital entryCapital);

    /**
     *
     * @param orderId  资匹的退货闭单接口
     */
    void closeOrder(String orderId);
}
