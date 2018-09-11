package com.jzfq.retail.core.call.service;

import com.jzfq.retail.core.call.domain.AssetEntryCapital;
import com.jzfq.retail.core.call.domain.OrderCheck;
import com.jzfq.retail.core.call.domain.RiskReceive;
import com.jzfq.retail.core.call.domain.RiskResult;

import java.util.List;

/**
 * @Author liuwei@juzifenqi.com
 * @Date 2018年07月2日 17:43
 * @Description: 风控对外service接口
 */
public interface RiskForeignService {

    /**
     * 进件前查看用户是否有进件资格（风控重构 替换checkMakeOrder接口）  准入接口
     *
     * @param orderCheck
     * @return
     * @throws Exception
     */
    String fkOrderCheck(OrderCheck orderCheck) throws Exception;

    /**
     * 新订单进件风控审核（风控重构）  进件接口
     *
     * @param riskReceive
     * @return
     * @throws Exception
     */
    void newRiskPushOrder(RiskReceive riskReceive) throws Exception;

    /**
     * 从风控系统获取通讯录
     *
     * @param idCard
     * @param name
     * @param mobile
     * @return
     */
    List<AssetEntryCapital.EntryTelbooks> getEntryTelbooks(String idCard, String name, String mobile, String orderSn);
}
